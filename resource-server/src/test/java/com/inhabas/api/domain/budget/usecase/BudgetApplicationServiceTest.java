package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.budget.ApplicationCannotModifiableException;
import com.inhabas.api.domain.budget.ApplicationNotFoundException;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BudgetApplicationServiceTest {

    @InjectMocks
    private BudgetApplicationServiceImpl budgetApplicationService;

    @Mock
    private BudgetApplicationRepository repository;

    private StudentId applicantId;

    @BeforeEach
    public void setUp() {
//        applicantId = MemberTest.basicMember1().getId();
    }

    @DisplayName("예산 지원 신청서를 제출한다.")
    @Test
    public void registerBudgetApplicationTest() {
        //given
        BudgetApplicationRegisterForm form = new BudgetApplicationRegisterForm("스터디 운영비",
                LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                "간식비", 10000, "k뱅크 1234556 홍길동");

        given(repository.save(any(BudgetSupportApplication.class))).willReturn(null);

        //when
        budgetApplicationService.registerApplication(form, applicantId);

        //then
        then(repository).should(times(1)).save(any(BudgetSupportApplication.class));
    }

    @DisplayName("예산 지원 신청서를 수정한다.")
    @Test
    public void updateBudgetApplicationTest() {
        //given
        BudgetSupportApplication original = BudgetSupportApplication.builder()
                .title("스터디 운영비")
                .details("과자")
                .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
                .outcome(10000)
                .account("국민 1234 홍길동")
                .applicationWriter(applicantId)
                .build();
        ReflectionTestUtils.setField(original, "id", 1);
        given(repository.findById(anyInt())).willReturn(Optional.of(original));

        given(repository.save(any())).willReturn(null);
        BudgetApplicationUpdateForm form = new BudgetApplicationUpdateForm("스터디 운영비",
                LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                "간식비", 10000, "k뱅크 1234556 홍길동", 13);

        //when
        budgetApplicationService.updateApplication(form, applicantId);

        //then
        then(repository).should(times(1)).save(any(BudgetSupportApplication.class));
    }

    @DisplayName("본인이 작성하지 않은 신청서는 수정할 수 없다.")
    @Test
    public void cannotUpdateOtherApplicationsTest() {
        //given
        BudgetSupportApplication original = BudgetSupportApplication.builder()
                .title("스터디 운영비")
                .details("과자")
                .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
                .outcome(10000)
                .account("국민 1234 홍길동")
                .applicationWriter(applicantId)
                .build();
        ReflectionTestUtils.setField(original, "id", 1);
        given(repository.findById(anyInt())).willReturn(Optional.of(original));

        BudgetApplicationUpdateForm form = new BudgetApplicationUpdateForm("스터디 운영비",
                LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                "간식비", 10000, "k뱅크 1234556 홍길동", 1);

        //when
        Assertions.assertThrows(ApplicationCannotModifiableException.class,
                () -> budgetApplicationService.updateApplication(form, new StudentId("12")));

        //then
        then(repository).should(times(1)).findById(anyInt());
        then(repository).should(times(0)).save(any(BudgetSupportApplication.class));
    }

    @DisplayName("수정 시 존재하지 않는 신청서 id 이면 NotFoundException 을 발생시킨다.")
    @Test
    public void throwNotFoundExceptionWhenUpdating() {
        //given
        BudgetApplicationUpdateForm form = new BudgetApplicationUpdateForm("스터디 운영비",
                LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                "간식비", 10000, "k뱅크 1234556 홍길동", 13);
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        //when
        Assertions.assertThrows(ApplicationNotFoundException.class,
                () -> budgetApplicationService.updateApplication(form, applicantId));

        //then
        then(repository).should(times(1)).findById(anyInt());
        then(repository).should(times(0)).save(any(BudgetSupportApplication.class));
    }

    @DisplayName("예산 지원 신청서를 삭제한다.")
    @Test
    public void deleteBudgetApplicationTest() {
        //given
        BudgetSupportApplication original = BudgetSupportApplication.builder()
                .title("스터디 운영비")
                .details("과자")
                .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
                .outcome(10000)
                .account("국민 1234 홍길동")
                .applicationWriter(applicantId)
                .build();
        ReflectionTestUtils.setField(original, "id", 1);

        given(repository.findById(anyInt())).willReturn(Optional.of(original));
        doNothing().when(repository).deleteById(anyInt());

        //when
        budgetApplicationService.deleteApplication(1, applicantId);

        //then
        then(repository).should(times(1)).deleteById(anyInt());
    }

    @DisplayName("본인이 작성하지 않은 신청서는 삭제할 수 없다.")
    @Test
    public void cannotDeleteOtherApplicationsTest() {
        //given
        BudgetSupportApplication original = BudgetSupportApplication.builder()
                .title("스터디 운영비")
                .details("과자")
                .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
                .outcome(10000)
                .account("국민 1234 홍길동")
                .applicationWriter(applicantId)
                .build();
        ReflectionTestUtils.setField(original, "id", 1);
        given(repository.findById(anyInt())).willReturn(Optional.of(original));

        //when
        Assertions.assertThrows(ApplicationCannotModifiableException.class,
                () -> budgetApplicationService.deleteApplication(1, new StudentId("12")));

        //then
        then(repository).should(times(1)).findById(anyInt());
        then(repository).should(times(0)).deleteById(anyInt());
    }

    @DisplayName("삭제 시 존재하지 않는 신청서 id 이면 NotFoundException 을 발생시킨다.")
    @Test
    public void throwNotFoundExceptionWhenDeleting() {
        //given
        given(repository.findById(anyInt())).willReturn(Optional.empty());

        //when
        Assertions.assertThrows(ApplicationNotFoundException.class,
                () -> budgetApplicationService.deleteApplication(1, applicantId));

        //then
        then(repository).should(times(1)).findById(anyInt());
        then(repository).should(times(0)).deleteById(anyInt());
    }

    @DisplayName("예산 지원 신청서를 조회한다.")
    @Test
    public void getBudgetApplicationDetailsTest() {
        //given
        BudgetApplicationDetailDto detailDto =
                new BudgetApplicationDetailDto(1, "",
                        LocalDateTime.of(2020,1,1,1,1,1),
                        LocalDateTime.of(2020,6,3,2,1,1),
                        "", 10000, "",
                        12171652, "유동현",
                        12345678, "김민겸",
                        ApplicationStatus.WAITING, "");
        given(repository.findDtoById(anyInt())).willReturn(Optional.of(detailDto));

        //when
        budgetApplicationService.getApplicationDetails(1);

        //then
        then(repository).should(times(1)).findDtoById(anyInt());
    }

    @DisplayName("id 에 해당하는 예산 지원 신청서가 존재하지 않으면 NotFoundException 을 던진다.")
    @Test
    public void throwNotFoundExceptionWhenGetting() {
        //given
        given(repository.findDtoById(anyInt())).willReturn(Optional.empty());

        //when
        Assertions.assertThrows(ApplicationNotFoundException.class,
                ()-> budgetApplicationService.getApplicationDetails(1));

        //then
        then(repository).should(times(1)).findDtoById(anyInt());
    }

    @DisplayName("예산 지원 신청서 목록을 조회한다.")
    @Test
    public void getBudgetApplicationListTest() {
        //given
        given(repository.search(any(), any(Pageable.class))).willReturn(Page.empty());

        //when
        budgetApplicationService.getApplications(null, Pageable.ofSize(15));

        //then
        then(repository).should(times(1)).search(any(), any(Pageable.class));
    }
}
