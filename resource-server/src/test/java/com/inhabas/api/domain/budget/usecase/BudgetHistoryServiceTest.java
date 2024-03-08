package com.inhabas.api.domain.budget.usecase;

import static com.inhabas.api.auth.domain.error.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuExampleTest;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class BudgetHistoryServiceTest {

  @InjectMocks private BudgetHistoryServiceImpl budgetHistoryService;
  @Mock private BudgetHistoryRepository budgetHistoryRepository;
  @Mock private MemberRepository memberRepository;
  @Mock private MenuRepository menuRepository;
  @Mock private S3Service s3Service;

  private static final String HISTORY_TITLE = "title";
  private static final String HISTORY_DETAILS = "details";
  private static final Integer HISTORY_OUTCOME = 10000;
  private static final String ACCOUNT_NUMBER = "123-123-123";
  private static final LocalDateTime HISTORY_DATE_USED = LocalDateTime.now().minusDays(1L);

  @DisplayName("총무가 회계내역을 추가한다.")
  @Test
  public void createHistoryTest() {
    Member secretary = mock(Member.class);
    Member memberReceived = mock(Member.class);
    BudgetHistoryCreateForm form = mock(BudgetHistoryCreateForm.class);
    Menu menu = mock(Menu.class);
    BudgetHistory budgetHistory =
        BudgetHistory.builder()
            .title("title")
            .menu(menu)
            .details("details")
            .dateUsed(LocalDateTime.now())
            .memberInCharge(secretary)
            .account(null)
            .income(1000)
            .outcome(0)
            .memberReceived(secretary)
            .build();

    given(memberRepository.findById(any())).willReturn(Optional.of(secretary));
    given(memberRepository.findByStudentId_IdAndName_Value(any(), any()))
        .willReturn(Optional.of(memberReceived));
    given(menuRepository.findById(anyInt())).willReturn(Optional.of(menu));
    given(budgetHistoryRepository.save(any())).willReturn(budgetHistory);
    given(form.toEntity(any(), any(), any())).willReturn(budgetHistory);

    // when
    budgetHistoryService.createHistory(form, null, 1L);

    // then
    then(budgetHistoryRepository).should(times(1)).save(any());
  }

  @DisplayName("총무가 회계내역을 수정한다.")
  @Test
  public void modifyHistoryTest() {
    // given
    Member secretary = MemberTest.secretaryMember();
    ReflectionTestUtils.setField(secretary, "id", 1L);
    Member memberReceived = MemberTest.basicMember1();
    MenuGroup menuGroup = MenuGroupExampleTest.getBudgetMenuGroup();
    Menu menu = MenuExampleTest.getBudgetHistoryMenu(menuGroup);
    BudgetHistoryCreateForm form =
        new BudgetHistoryCreateForm(
            LocalDateTime.now().minusDays(1L), "title", "details", "12171234", "유동현", 0, 10000);
    BudgetHistory budgetHistory =
        BudgetHistory.builder()
            .title("title")
            .menu(menu)
            .details("details")
            .dateUsed(LocalDateTime.now())
            .memberInCharge(secretary)
            .account(null)
            .income(1000)
            .outcome(0)
            .memberReceived(secretary)
            .build()
            .writtenBy(secretary, BudgetHistory.class);
    ReflectionTestUtils.setField(budgetHistory, "id", 1L);
    given(memberRepository.findById(any())).willReturn(Optional.of(secretary));
    given(memberRepository.findByStudentId_IdAndName_Value(any(), any()))
        .willReturn(Optional.of(memberReceived));
    given(budgetHistoryRepository.findById(any())).willReturn(Optional.of(budgetHistory));
    given(budgetHistoryRepository.save(any())).willReturn(budgetHistory);

    // when
    budgetHistoryService.modifyHistory(1L, form, null, 1L);

    // then
    then(budgetHistoryRepository).should(times(1)).save(any(BudgetHistory.class));
  }

  @DisplayName("총무가 회계 내역을 삭제한다.")
  @Test
  public void deleteHistoryTest() {
    // given
    Member secretary = mock(Member.class);
    Menu menu = mock(Menu.class);
    BudgetHistory budgetHistory =
        BudgetHistory.builder()
            .title("title")
            .menu(menu)
            .details("details")
            .dateUsed(LocalDateTime.now())
            .memberInCharge(secretary)
            .account(null)
            .income(1000)
            .outcome(0)
            .memberReceived(secretary)
            .build()
            .writtenBy(secretary, BudgetHistory.class);
    given(memberRepository.findById(any())).willReturn(Optional.of(secretary));
    given(budgetHistoryRepository.findById(any())).willReturn(Optional.of(budgetHistory));

    // when
    budgetHistoryService.deleteHistory(1L, 1L);

    // then
    then(budgetHistoryRepository).should(times(1)).findById(any());
    then(budgetHistoryRepository).should(times(1)).deleteById(any());
  }

  @DisplayName("존재하지 않는 기록을 삭제하려고 하면 예외를 던진다.")
  @Test
  public void raiseNotFoundExceptionWhenDeletingTest() {
    // given
    Member secretary = mock(Member.class);
    given(budgetHistoryRepository.findById(any())).willReturn(Optional.empty());
    given(memberRepository.findById(any())).willReturn(Optional.of(secretary));

    // when
    assertThatThrownBy(() -> budgetHistoryService.deleteHistory(1L, 1L))
        .isInstanceOf(NotFoundException.class)
        .hasMessage(NOT_FOUND.getMessage());

    // then
    then(budgetHistoryRepository).should(times(1)).findById(any());
  }

  @DisplayName("회계내역 리스트를 조회한다.")
  @Test
  public void getListOfBudgetHistoryTest() {
    // given
    given(budgetHistoryRepository.search(any())).willReturn(null);

    // when
    budgetHistoryService.searchHistoryList(null);

    // then
    then(budgetHistoryRepository).should(times(1)).search(any());
  }

  @DisplayName("회계내역을 id 로 조회한다.")
  @Test
  public void getOneBudgetHistoryTest() {
    // given
    Member secretary = MemberTest.secretaryMember();
    MenuGroup menuGroup = MenuGroupExampleTest.getBudgetMenuGroup();
    Menu menu = MenuExampleTest.getBudgetHistoryMenu(menuGroup);
    BudgetHistory history =
        BudgetHistory.builder()
            .title(HISTORY_TITLE)
            .menu(menu)
            .details(HISTORY_DETAILS)
            .dateUsed(HISTORY_DATE_USED)
            .account(ACCOUNT_NUMBER)
            .income(0)
            .outcome(HISTORY_OUTCOME)
            .memberInCharge(secretary)
            .memberReceived(secretary)
            .build()
            .writtenBy(secretary, BudgetHistory.class);
    given(budgetHistoryRepository.findById(any())).willReturn(Optional.of(history));

    // when
    budgetHistoryService.getHistory(1L);

    // then
    then(budgetHistoryRepository).should(times(1)).findById(any());
  }

  @DisplayName("id 에 해당하는 회계내역이 없는 경우, NotFoundException을 던진다.")
  @Test
  public void cannotFindBudgetHistoryFindById() {
    // given
    given(budgetHistoryRepository.findById(any())).willReturn(Optional.empty());

    // when
    assertThatThrownBy(() -> budgetHistoryService.getHistory(1L))
        .isInstanceOf(NotFoundException.class)
        .hasMessage(NOT_FOUND.getMessage());

    then(budgetHistoryRepository).should(times(1)).findById(any());
  }

  @DisplayName("기록이 존재하는 년도를 모두 가져온다.")
  @Test
  public void fetchAllYearOfHistoryTest() {
    // given
    given(budgetHistoryRepository.findAllYear()).willReturn(null);

    // when
    budgetHistoryService.getAllYearOfHistory();

    // then
    then(budgetHistoryRepository).should(times(1)).findAllYear();
  }
}
