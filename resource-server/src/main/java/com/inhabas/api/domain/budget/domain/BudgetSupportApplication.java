package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.budget.ApplicationCannotModifiableException;
import com.inhabas.api.domain.budget.ApplicationNotFoundException;
import com.inhabas.api.domain.budget.domain.converter.StatusConverter;
import com.inhabas.api.domain.budget.domain.valueObject.*;

@Entity
@Table(name = "budget_support_application")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetSupportApplication extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Title title;

  private LocalDateTime dateUsed;

  private Details details;

  @AttributeOverride(name = "value", column = @Column(name = "outcome", nullable = false))
  private Price outcome;

  private ApplicantAccount applicantAccount;

  @AttributeOverride(name = "id", column = @Column(nullable = false, name = "applicant"))
  private StudentId applicationWriter;

  @AttributeOverride(name = "id", column = @Column(name = "person_in_charge"))
  private StudentId personInCharge;

  @Convert(converter = StatusConverter.class)
  @Column(nullable = false)
  private ApplicationStatus status;

  private RejectReason rejectReason;

  // private List<File> receipts;

  @Builder
  public BudgetSupportApplication(
      String title,
      LocalDateTime dateUsed,
      String details,
      Integer outcome,
      String account,
      StudentId applicationWriter) {
    this.title = new Title(title);
    this.dateUsed = dateUsed;
    this.details = new Details(details);
    this.outcome = new Price(outcome);
    this.applicantAccount = new ApplicantAccount(account);
    this.applicationWriter = applicationWriter;
    this.status = ApplicationStatus.WAITING;
  }

  public void modify(
      String title,
      LocalDateTime dateUsed,
      String details,
      Integer outcome,
      String account,
      StudentId currentApplicant) {

    if (this.id == null)
      throw new ApplicationNotFoundException(
          "cannot modify this entity, because not persisted ever!");

    if (this.cannotModifiableBy(currentApplicant)) throw new ApplicationCannotModifiableException();

    this.title = new Title(title);
    this.dateUsed = dateUsed;
    this.details = new Details(details);
    this.outcome = new Price(outcome);
    this.applicantAccount = new ApplicantAccount(account);
    this.status = ApplicationStatus.WAITING;
  }

  public boolean cannotModifiableBy(StudentId currentApplicant) {
    return !this.applicationWriter.equals(currentApplicant);
  }

  public void approve(StudentId personInCharge) {

    this.status = ApplicationStatus.APPROVED;
    this.personInCharge = personInCharge;
  }

  public void waiting(StudentId personInCharge) {

    this.status = ApplicationStatus.WAITING;
    this.personInCharge = personInCharge;
  }

  public void deny(String reason, StudentId personInCharge) {

    this.rejectReason = new RejectReason(reason);
    this.status = ApplicationStatus.DENIED;
    this.personInCharge = personInCharge;
  }

  public void process(StudentId personInCharge) {

    if (this.isProcessed())
      throw new ApplicationCannotModifiableException("이미 처리가 완료된 예산지원 내역입니다.");

    this.status = ApplicationStatus.PROCESSED;
    this.personInCharge = personInCharge;
  }

  private boolean isProcessed() {
    return this.status == ApplicationStatus.PROCESSED;
  }

  /** 예산지원신청을 완전히 다 처리하고 나면 자동적으로 회계처리내역에 추가되도록 하기 위해서, 회계내역 엔티티로 변환한다. */
  public BudgetHistory makeHistory() {

    if (this.isProcessed() && this.personInCharge != null) {
      return BudgetHistory.builder()
          .title(this.title.getValue())
          .dateUsed(this.dateUsed)
          .details(this.details.getValue())
          .income(0)
          .outcome(this.outcome.getValue())
          .personInCharge(this.personInCharge)
          .personReceived(this.applicationWriter)
          .build();
    } else {
      throw new RuntimeException("회계기록 중에 오류가 발생하였습니다!");
    }
  }
}
