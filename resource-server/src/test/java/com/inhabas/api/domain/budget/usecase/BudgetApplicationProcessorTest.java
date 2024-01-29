package com.inhabas.api.domain.budget.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.domain.valueObject.RejectReason;
import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class BudgetApplicationProcessorTest {

  @InjectMocks private BudgetApplicationProcessorImpl processor;

  @Mock private BudgetApplicationRepository applicationRepository;
  @Mock private BudgetHistoryRepository historyRepository;

  @DisplayName("대기중인 요청을 승인한다.")
  @Test
  public void waitingToApproveTest() {
    // given
    BudgetSupportApplication application =
        BudgetSupportApplication.builder()
            .title("title")
            .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
            .details("details")
            .outcome(10000)
            .applicationWriter(new StudentId("12171652"))
            .account("기업 1234 홍길동")
            .build();
    given(applicationRepository.findById(anyInt())).willReturn(Optional.of(application));

    // when
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest("APPROVED", null);
    processor.process(1, request, new StudentId("18165249"));

    // then
    then(applicationRepository).should(times(1)).findById(anyInt());
    ApplicationStatus status =
        (ApplicationStatus) ReflectionTestUtils.getField(application, "status");
    assertThat(status).isEqualTo(ApplicationStatus.APPROVED);
  }

  @DisplayName("대기중인 요청을 거절한다.")
  @Test
  public void waitingToDenyTest() {
    // given
    BudgetSupportApplication application =
        BudgetSupportApplication.builder()
            .title("title")
            .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
            .details("details")
            .outcome(10000)
            .applicationWriter(new StudentId("12171652"))
            .account("기업 1234 홍길동")
            .build();
    given(applicationRepository.findById(anyInt())).willReturn(Optional.of(application));

    // when
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest("DENIED", "중복 요청");
    processor.process(1, request, new StudentId("18165249"));

    // then
    then(applicationRepository).should(times(1)).findById(anyInt());
    ApplicationStatus status =
        (ApplicationStatus) ReflectionTestUtils.getField(application, "status");
    assertThat(status).isEqualTo(ApplicationStatus.DENIED);
  }

  @DisplayName("대기중인 요청을 거절할 때, 거절 사유는 필수이다.")
  @Test
  public void rejectReasonIsNecessaryToDenyTest() {
    // given
    BudgetSupportApplication application =
        BudgetSupportApplication.builder()
            .title("title")
            .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
            .details("details")
            .outcome(10000)
            .applicationWriter(new StudentId("12171652"))
            .account("기업 1234 홍길동")
            .build();
    given(applicationRepository.findById(anyInt())).willReturn(Optional.of(application));

    // when
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest("DENIED", null);

    // then
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> processor.process(1, request, new StudentId("18165249")));
    then(applicationRepository).should(times(1)).findById(anyInt());
  }

  @DisplayName("대기중인 요청을 완료처리한다.")
  @Test
  public void waitingToProcessTest() {
    // given
    BudgetSupportApplication application =
        BudgetSupportApplication.builder()
            .title("title")
            .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
            .details("details")
            .outcome(10000)
            .applicationWriter(new StudentId("12171652"))
            .account("기업 1234 홍길동")
            .build();
    given(applicationRepository.findById(anyInt())).willReturn(Optional.of(application));
    given(historyRepository.save(any(BudgetHistory.class))).willReturn(null);

    // when
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest("PROCESSED", null);
    processor.process(1, request, new StudentId("18165249"));

    // then
    then(applicationRepository).should(times(1)).findById(anyInt());
    then(historyRepository).should(times(1)).save(any(BudgetHistory.class));
    ApplicationStatus status =
        (ApplicationStatus) ReflectionTestUtils.getField(application, "status");
    assertThat(status).isEqualTo(ApplicationStatus.PROCESSED);
  }

  @DisplayName("거절했던 신청이 수정된 뒤에 승인한다.")
  @Test
  public void deniedToApprovedTest() {
    // given
    BudgetSupportApplication application =
        BudgetSupportApplication.builder()
            .title("title")
            .dateUsed(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
            .details("details")
            .outcome(10000)
            .applicationWriter(new StudentId("12171652"))
            .account("기업 1234 홍길동")
            .build();
    ReflectionTestUtils.setField(application, "status", ApplicationStatus.DENIED);
    ReflectionTestUtils.setField(application, "rejectReason", new RejectReason("계좌번호 미기재"));
    given(applicationRepository.findById(anyInt())).willReturn(Optional.of(application));

    // when
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest("APPROVED", null);
    processor.process(1, request, new StudentId("18165249"));

    // then
    then(applicationRepository).should(times(1)).findById(anyInt());
    ApplicationStatus status =
        (ApplicationStatus) ReflectionTestUtils.getField(application, "status");
    assertThat(status).isEqualTo(ApplicationStatus.APPROVED);
  }
}
