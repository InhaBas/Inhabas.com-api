package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.budget.HistoryCannotModifiableException;
import com.inhabas.api.domain.budget.BudgetHistoryNotFoundException;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BudgetHistoryServiceTest {

    @InjectMocks
    private BudgetHistoryServiceImpl budgetHistoryService;

    @Mock
    private BudgetHistoryRepository repository;

    @DisplayName("총무가 회계내역을 추가한다.")
    @Test
    public void createBudgetHistoryTest() {

        //given
        BudgetHistoryCreateForm form = new BudgetHistoryCreateForm(
                LocalDateTime.of(2000, 1, 1, 1, 1, 1),
                "서버운영비", "aws 작년 서버비용", "12345678", 0, 500000);
        StudentId cfo = new StudentId("12171652");
        given(repository.save(any(BudgetHistory.class))).willReturn(null);

        //when
        budgetHistoryService.createNewHistory(form, cfo);

        //then
        then(repository).should(times(1)).save(any(BudgetHistory.class));
    }

    @DisplayName("총무가 회계내역을 수정한다.")
    @Test
    public void modifyBudgetHistoryTest() {
        //when
        StudentId CFO = new StudentId("12171652");
        BudgetHistory history = BudgetHistory.builder()
                .title("서버 운영비")
                .details("작년 aws 운영 비용")
                .income(0)
                .outcome(500000)
                .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                .personReceived(new StudentId("10982942"))
                .personInCharge(CFO)
                .build();
        ReflectionTestUtils.setField(history, "id", 1);
        given(repository.findById(any())).willReturn(Optional.of(history));

        //when
        BudgetHistoryModifyForm form = new BudgetHistoryModifyForm(
                LocalDateTime.of(2000, 1, 1, 1, 1, 1),
                "서버운영비", "aws 작년 서버비용", "12345678", 0, 500000, 1);
        budgetHistoryService.modifyHistory(form, CFO);

        //then
        then(repository).should(times(1)).findById(any());
        then(repository).should(times(1)).save(any(BudgetHistory.class));
    }

    @DisplayName("총무는 이전의 다른 총무가 작성한 내역을 수정할 수 없다.")
    @Test
    public void cannotModifyBudgetHistoryOfOtherCFO() {
        //given
        StudentId previousCFO = new StudentId("12171652");
        StudentId currentCFO = new StudentId("99999999");
        BudgetHistory history = BudgetHistory.builder()
                .title("서버 운영비")
                .details("작년 aws 운영 비용")
                .income(0)
                .outcome(500000)
                .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                .personReceived(new StudentId("10982942"))
                .personInCharge(previousCFO)
                .build();
        ReflectionTestUtils.setField(history, "id", 1);
        given(repository.findById(any())).willReturn(Optional.of(history));

        //when
        BudgetHistoryModifyForm form = new BudgetHistoryModifyForm(
                LocalDateTime.of(2000, 1, 1, 1, 1, 1),
                "서버운영비", "aws 작년 서버비용", "12345678", 0, 500000, 1);
        Assertions.assertThrows(AccessDeniedException.class,
                () -> budgetHistoryService.modifyHistory(form, currentCFO));
    }

    @DisplayName("수정 시도할 때 히스토리 내역이 존재하지 않으면 오류를 던진다.")
    @Test
    public void raiseNotFoundExceptionWhenModifyingTest() {
        //given
        given(repository.findById(any())).willReturn(Optional.empty());

        //when
        BudgetHistoryModifyForm form = new BudgetHistoryModifyForm(
                LocalDateTime.of(2000, 1, 1, 1, 1, 1),
                "서버운영비", "aws 작년 서버비용", "12345678", 0, 500000, 1);
        Assertions.assertThrows(BudgetHistoryNotFoundException.class,
                () -> budgetHistoryService.modifyHistory(form, new StudentId("12171652")));
    }

    @DisplayName("총무가 회계 내역을 삭제한다.")
    @Test
    public void deleteBudgetHistoryTest() {
        //given
        StudentId CFO = new StudentId("12171652");
        Integer historyId = 1;
        BudgetHistory history = BudgetHistory.builder()
                .title("서버 운영비")
                .details("작년 aws 운영 비용")
                .income(0)
                .outcome(500000)
                .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                .personReceived(new StudentId("10982942"))
                .personInCharge(CFO)
                .build();
        ReflectionTestUtils.setField(history, "id", historyId);
        given(repository.findById(any())).willReturn(Optional.of(history));

        //when
        budgetHistoryService.deleteHistory(historyId, CFO);

        //then
        then(repository).should(times(1)).findById(anyInt());
        then(repository).should(times(1)).deleteById(anyInt());
    }

    @DisplayName("이전의 다른 담당자가 작성했던 기록은 삭제할 수 없다.")
    @Test
    public void cannotDeleteBudgetHistoryTest() {
        //given
        StudentId previousCFO = new StudentId("12171652");
        StudentId currentCFO = new StudentId("99999999");
        Integer historyId = 1;
        BudgetHistory history = BudgetHistory.builder()
                .title("서버 운영비")
                .details("작년 aws 운영 비용")
                .income(0)
                .outcome(500000)
                .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                .personReceived(new StudentId("10982942"))
                .personInCharge(previousCFO)
                .build();
        ReflectionTestUtils.setField(history, "id", historyId);
        given(repository.findById(any())).willReturn(Optional.of(history));

        //when
        Assertions.assertThrows(HistoryCannotModifiableException.class,
                () -> budgetHistoryService.deleteHistory(historyId, currentCFO));

        //then
        then(repository).should(times(1)).findById(anyInt());
    }

    @DisplayName("존재하지 않는 기록을 삭제하려고 하면 예외를 던진다.")
    @Test
    public void raiseNotFoundExceptionWhenDeletingTest() {
        //given
        given(repository.findById(any())).willReturn(Optional.empty());

        //when
        Assertions.assertThrows(BudgetHistoryNotFoundException.class,
                () -> budgetHistoryService.deleteHistory(1, new StudentId("12171652")));

        //then
        then(repository).should(times(1)).findById(anyInt());
    }

    @DisplayName("회계내역을 한 페이지를 출력한다.")
    @Test
    public void getListOfBudgetHistoryTest() {
        //given
        given(repository.search(any(), any())).willReturn(null);

        //when
        budgetHistoryService.searchHistoryList(null, Pageable.ofSize(15));

        //then
        then(repository).should(times(1)).search(any(), any());
    }

    @DisplayName("회계내역을 id 로 조회한다.")
    @Test
    public void getOneBudgetHistoryTest() {
        //given
        given(repository.findDtoById(anyInt()))
                .willReturn(Optional.of(new BudgetHistoryDetailDto(
                        null, null, null, null, null, null,
                        null, null, null, null,
                        null, null, null)));

        //when
        budgetHistoryService.getHistory(2);

        //then
        then(repository).should(times(1)).findDtoById(anyInt());
    }

    @DisplayName("id 에 해당하는 회계내역이 없는 경우, NotFoundException을 던진다.")
    @Test
    public void cannotFindBudgetHistoryFindById() {
        //given
        given(repository.findDtoById(anyInt())).willReturn(Optional.empty());

        //when
        Assertions.assertThrows(BudgetHistoryNotFoundException.class,
                () -> budgetHistoryService.getHistory(2));

        then(repository).should(times(1)).findDtoById(anyInt());
    }

    @DisplayName("기록이 존재하는 년도를 모두 가져온다.")
    @Test
    public void fetchAllYearOfHistoryTest() {
        //given
        given(repository.findAllYear()).willReturn(null);

        //when
        budgetHistoryService.getAllYearOfHistory();

        //then
        then(repository).should(times(1)).findAllYear();
    }
}
