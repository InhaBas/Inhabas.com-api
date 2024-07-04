package com.inhabas.api.domain.budget.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.domain.budget.exception.StatusNotFollowProceduresException;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuExampleTest;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class BudgetApplicationProcessorTest {

  @InjectMocks private BudgetApplicationProcessorImpl budgetApplicationProcessor;

  @Mock private BudgetApplicationRepository applicationRepository;
  @Mock private BudgetHistoryRepository historyRepository;
  @Mock private MemberRepository memberRepository;
  @Mock private MenuRepository menuRepository;

  private static final String APPLICATION_TITLE = "title";
  private static final String APPLICATION_DETAILS = "details";
  private static final String ACCOUNT_NUMBER = "123-123-123";
  private static final Integer APPLICATION_OUTCOME = 10000;
  private static final RequestStatus INITIAL_REQUEST_STATUS = RequestStatus.PENDING;

  private Member secretary;
  private Member applicant;
  private MenuGroup menuGroup;
  private Menu menu;
  private BudgetSupportApplication application;

  @BeforeEach
  public void setUp() {
    secretary = MemberTest.secretaryMember();
    ReflectionTestUtils.setField(secretary, "id", 1L);
    applicant = MemberTest.basicMember1();
    menuGroup = MenuGroupExampleTest.getBudgetMenuGroup();
    menu = MenuExampleTest.getBudgetHistoryMenu(menuGroup);
    application =
        new BudgetSupportApplication(
            menu,
            APPLICATION_TITLE,
            APPLICATION_DETAILS,
            LocalDateTime.now().minusDays(1),
            ACCOUNT_NUMBER,
            APPLICATION_OUTCOME,
            applicant,
            INITIAL_REQUEST_STATUS);
    given(memberRepository.findById(any())).willReturn(Optional.of(secretary));
    given(menuRepository.findById(anyInt())).willReturn(Optional.of(menu));
    given(applicationRepository.findById(any())).willReturn(Optional.of(application));
  }

  @DisplayName("대기중인 요청을 승인한다.")
  @Transactional
  @Test
  public void waitingToApproveTest() {
    // given
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest(RequestStatus.APPROVED, null);

    // when
    budgetApplicationProcessor.process(1L, request, 1L);

    // then
    then(applicationRepository).should(times(1)).findById(any());
    RequestStatus status = (RequestStatus) ReflectionTestUtils.getField(application, "status");
    assertThat(status).isEqualTo(RequestStatus.APPROVED);
  }

  @DisplayName("대기중인 요청을 거절한다.")
  @Transactional
  @Test
  public void waitingToDenyTest() {
    // given
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest(RequestStatus.REJECTED, "reject");

    // when
    budgetApplicationProcessor.process(1L, request, 1L);

    // then
    then(applicationRepository).should(times(1)).findById(any());
    RequestStatus status = (RequestStatus) ReflectionTestUtils.getField(application, "status");
    assertThat(status).isEqualTo(RequestStatus.REJECTED);
  }

  @DisplayName("대기중인 요청을 거절할 때, 거절 사유는 필수이다.")
  @Transactional
  @Test
  public void rejectReasonIsNecessaryToDenyTest() {
    // given
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest(RequestStatus.REJECTED, null);

    // then
    assertThatThrownBy(() -> budgetApplicationProcessor.process(1L, request, 1L))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage(ErrorCode.INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("대기중인 요청을 완료처리한다.")
  @Transactional
  @Test
  public void waitingToProcessTest() {
    // given
    application =
        new BudgetSupportApplication(
            menu,
            APPLICATION_TITLE,
            APPLICATION_DETAILS,
            LocalDateTime.now().minusDays(1),
            ACCOUNT_NUMBER,
            APPLICATION_OUTCOME,
            applicant,
            RequestStatus.APPROVED);
    BudgetHistory history =
        new BudgetHistory(
            "title",
            menu,
            "datails",
            LocalDateTime.now().minusDays(1),
            secretary,
            ACCOUNT_NUMBER,
            0,
            APPLICATION_OUTCOME,
            applicant);
    given(applicationRepository.findById(any())).willReturn(Optional.of(application));
    given(historyRepository.save(any())).willReturn(history);
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest(RequestStatus.COMPLETED, null);

    // when
    budgetApplicationProcessor.process(1L, request, 1L);

    // then
    then(applicationRepository).should(times(1)).findById(any());
    then(historyRepository).should(times(1)).save(any(BudgetHistory.class));
    RequestStatus status = (RequestStatus) ReflectionTestUtils.getField(application, "status");
    assertThat(status).isEqualTo(RequestStatus.COMPLETED);
  }

  @DisplayName("거절했던 신청은 되돌리지 못한다.")
  @Transactional
  @Test
  public void deniedToProcessTest() {
    // given
    application =
        new BudgetSupportApplication(
            menu,
            APPLICATION_TITLE,
            APPLICATION_DETAILS,
            LocalDateTime.now().minusDays(1),
            ACCOUNT_NUMBER,
            APPLICATION_OUTCOME,
            applicant,
            RequestStatus.COMPLETED);
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest(RequestStatus.REJECTED, "reject");

    // when
    budgetApplicationProcessor.process(1L, request, 1L);

    // then
    assertThatThrownBy(() -> budgetApplicationProcessor.process(1L, request, 1L))
        .isInstanceOf(StatusNotFollowProceduresException.class)
        .hasMessage(ErrorCode.STATUS_NOT_FOLLOW_PROCEDURES.getMessage());
  }
}
