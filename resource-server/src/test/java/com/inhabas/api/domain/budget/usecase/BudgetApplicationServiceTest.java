package com.inhabas.api.domain.budget.usecase;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
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
public class BudgetApplicationServiceTest {

  @InjectMocks private BudgetApplicationServiceImpl budgetApplicationService;

  @Mock private BudgetApplicationRepository budgetApplicationRepository;
  @Mock private MemberRepository memberRepository;
  @Mock private MenuRepository menuRepository;
  @Mock private S3Service s3Service;

  private static final String APPLICATION_TITLE = "title";
  private static final String APPLICATION_DETAILS = "details";
  private static final String APPLICATION_AFTER_DETAILS = "afterDetails";
  private static final String ACCOUNT_NUMBER = "123-123-123";
  private static final Integer APPLICATION_OUTCOME = 10000;
  private static final RequestStatus INITIAL_REQUEST_STATUS = RequestStatus.PENDING;

  @DisplayName("예산 지원 신청서를 제출한다.")
  @Test
  public void registerApplicationTest() {
    // given
    Member applicant = MemberTest.basicMember1();
    MenuGroup menuGroup = MenuGroupExampleTest.getBudgetMenuGroup();
    Menu menu = MenuExampleTest.getBudgetHistoryMenu(menuGroup);
    BudgetApplicationRegisterForm form =
        new BudgetApplicationRegisterForm(
            APPLICATION_TITLE,
            LocalDateTime.now().minusDays(1L),
            APPLICATION_DETAILS,
            APPLICATION_OUTCOME,
            ACCOUNT_NUMBER);
    BudgetSupportApplication budgetSupportApplication =
        new BudgetSupportApplication(
            menu,
            APPLICATION_TITLE,
            APPLICATION_DETAILS,
            LocalDateTime.now().minusDays(1),
            ACCOUNT_NUMBER,
            APPLICATION_OUTCOME,
            applicant,
            INITIAL_REQUEST_STATUS);
    given(memberRepository.findById(any())).willReturn(Optional.of(applicant));
    given(menuRepository.findById(anyInt())).willReturn(Optional.of(menu));
    given(budgetApplicationRepository.save(any(BudgetSupportApplication.class)))
        .willReturn(budgetSupportApplication);

    // when
    budgetApplicationService.registerApplication(form, null, 1L);

    // then
    then(budgetApplicationRepository).should(times(1)).save(any(BudgetSupportApplication.class));
  }

  @DisplayName("예산 지원 신청서를 수정한다.")
  @Test
  public void updateApplicationTest() {
    // given
    Member applicant = MemberTest.basicMember1();
    MenuGroup menuGroup = MenuGroupExampleTest.getBudgetMenuGroup();
    Menu menu = MenuExampleTest.getBudgetHistoryMenu(menuGroup);
    BudgetSupportApplication application =
        new BudgetSupportApplication(
            menu,
            APPLICATION_TITLE,
            APPLICATION_DETAILS,
            LocalDateTime.now().minusDays(1),
            ACCOUNT_NUMBER,
            APPLICATION_OUTCOME,
            applicant,
            INITIAL_REQUEST_STATUS);
    ReflectionTestUtils.setField(application, "id", 1L);
    BudgetSupportApplication newApplication =
        new BudgetSupportApplication(
            menu,
            APPLICATION_TITLE,
            APPLICATION_AFTER_DETAILS,
            LocalDateTime.now().minusDays(1),
            ACCOUNT_NUMBER,
            APPLICATION_OUTCOME,
            applicant,
            INITIAL_REQUEST_STATUS);
    BudgetApplicationRegisterForm form =
        new BudgetApplicationRegisterForm(
            APPLICATION_TITLE,
            LocalDateTime.now().minusDays(1L),
            APPLICATION_AFTER_DETAILS,
            APPLICATION_OUTCOME,
            ACCOUNT_NUMBER);

    given(memberRepository.findById(any())).willReturn(Optional.of(applicant));
    given(budgetApplicationRepository.findById(any())).willReturn(Optional.of(application));
    given(budgetApplicationRepository.save(any())).willReturn(newApplication);

    // when
    budgetApplicationService.updateApplication(1L, form, 1L);

    // then
    then(budgetApplicationRepository).should(times(1)).save(any(BudgetSupportApplication.class));
  }

  @DisplayName("수정 시 존재하지 않는 신청서 id 이면 NotFoundException 을 발생시킨다.")
  @Test
  public void throwNotFoundExceptionWhenUpdating() {
    // given
    Member applicant = MemberTest.basicMember1();
    BudgetApplicationRegisterForm form =
        new BudgetApplicationRegisterForm(
            APPLICATION_TITLE,
            LocalDateTime.now().minusDays(1L),
            APPLICATION_AFTER_DETAILS,
            APPLICATION_OUTCOME,
            ACCOUNT_NUMBER);
    given(budgetApplicationRepository.findById(any())).willReturn(Optional.empty());
    given(memberRepository.findById(any())).willReturn(Optional.of(applicant));
    // when
    assertThatThrownBy(() -> budgetApplicationService.updateApplication(1L, form, 1L))
        .isInstanceOf(NotFoundException.class)
        .hasMessage(ErrorCode.NOT_FOUND.getMessage());

    // then
    then(budgetApplicationRepository).should(times(1)).findById(any());
    then(budgetApplicationRepository).should(times(0)).save(any(BudgetSupportApplication.class));
  }

  @DisplayName("예산 지원 신청서를 삭제한다.")
  @Test
  public void deleteBudgetApplicationTest() {
    // when
    doNothing().when(budgetApplicationRepository).deleteById(any());
    budgetApplicationService.deleteApplication(1L, 1L);

    // then
    then(budgetApplicationRepository).should(times(1)).deleteById(any());
  }

  @DisplayName("예산 지원 신청서를 조회한다.")
  @Test
  public void getBudgetApplicationDetailsTest() {
    // given
    Member applicant = MemberTest.basicMember1();
    BudgetApplicationDetailDto detailDto =
        new BudgetApplicationDetailDto(
            1L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            APPLICATION_TITLE,
            APPLICATION_DETAILS,
            APPLICATION_OUTCOME,
            ACCOUNT_NUMBER,
            applicant,
            applicant,
            INITIAL_REQUEST_STATUS,
            null);

    given(budgetApplicationRepository.findDtoById(any())).willReturn(Optional.of(detailDto));

    // when
    budgetApplicationService.getApplicationDetails(1L);

    // then
    then(budgetApplicationRepository).should(times(1)).findDtoById(any());
  }

  @DisplayName("id 에 해당하는 예산 지원 신청서가 존재하지 않으면 NotFoundException 을 던진다.")
  @Test
  public void throwNotFoundExceptionWhenGetting() {
    // given
    given(budgetApplicationRepository.findDtoById(any())).willReturn(Optional.empty());

    // when
    assertThatThrownBy(() -> budgetApplicationService.getApplicationDetails(1L))
        .isInstanceOf(NotFoundException.class)
        .hasMessage(ErrorCode.NOT_FOUND.getMessage());
  }

  @DisplayName("예산 지원 신청서 목록을 조회한다.")
  @Test
  public void getBudgetApplicationListTest() {
    // given
    List<BudgetApplicationDto> dtoList = new ArrayList<>();
    Member applicant = MemberTest.basicMember1();
    BudgetApplicationDto budgetApplicationDto =
        new BudgetApplicationDto(
            1L, APPLICATION_TITLE, applicant, LocalDateTime.now(), INITIAL_REQUEST_STATUS);
    dtoList.add(budgetApplicationDto);
    given(budgetApplicationRepository.search(any())).willReturn(dtoList);

    // when
    budgetApplicationService.getApplications(null);

    // then
    then(budgetApplicationRepository).should(times(1)).search(any());
  }
}
