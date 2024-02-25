package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.budget.domain.converter.StatusConverter;
import com.inhabas.api.domain.budget.domain.valueObject.*;
import com.inhabas.api.domain.budget.exception.ApplicationCannotModifiableException;

@Entity
@Table(name = "BUDGET_SUPPORT_APPLICATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "BUDGET_APPLICATION")
public class BudgetSupportApplication extends BudgetBoard {

  @AttributeOverride(name = "value", column = @Column(name = "outcome", nullable = false))
  private Price outcome;

  private ApplicantAccount applicantAccount;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "USER_WRITER_ID",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_APPLICATION_WRITER"))
  private Member applicationWriter;

  @AttributeOverride(name = "id", column = @Column(name = "person_in_charge"))
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "USER_IN_CHARGE_ID",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_APPLICATION_IN_CHARGE"))
  private Member personInCharge;

  @Convert(converter = StatusConverter.class)
  @Column(nullable = false)
  private RequestStatus status;

  private RejectReason rejectReason;

  @Column(name = "DATE_CHECKED")
  private LocalDateTime dateChecked;

  @Builder
  public BudgetSupportApplication(
      String title,
      LocalDateTime dateUsed,
      String details,
      Integer outcome,
      String account,
      Member applicationWriter) {
    super(title, details, dateUsed, applicationWriter);
    this.outcome = new Price(outcome);
    this.applicantAccount = new ApplicantAccount(account);
    this.applicationWriter = applicationWriter;
    this.status = RequestStatus.PENDING;
  }

  public void modify(
      String title,
      LocalDateTime dateUsed,
      String details,
      Integer outcome,
      String account,
      Member applicationWriter) {

    if (this.id == null) {
      throw new NotFoundException();
    }

    if (this.cannotModifiableBy(applicationWriter))
      throw new ApplicationCannotModifiableException();

    this.title = new Title(title);
    this.dateUsed = dateUsed;
    this.details = new Details(details);
    this.outcome = new Price(outcome);
    this.applicantAccount = new ApplicantAccount(account);
    this.status = RequestStatus.PENDING;
  }

  public boolean cannotModifiableBy(Member currentApplicant) {
    return !this.applicationWriter.equals(currentApplicant);
  }

  public void approve(Member personInCharge) {

    this.status = RequestStatus.APPROVED;
    this.personInCharge = personInCharge;
  }

  public void pending(Member personInCharge) {

    this.status = RequestStatus.PENDING;
    this.personInCharge = personInCharge;
  }

  public void reject(String reason, Member personInCharge) {

    this.rejectReason = new RejectReason(reason);
    this.status = RequestStatus.REJECTED;
    this.personInCharge = personInCharge;
  }

  public void complete(Member personInCharge) {

    if (this.isComplete()) throw new ApplicationCannotModifiableException("이미 처리가 완료된 예산지원 내역입니다.");

    this.status = RequestStatus.COMPLETED;
    this.personInCharge = personInCharge;
  }

  private boolean isComplete() {
    return this.status == RequestStatus.COMPLETED;
  }

  /** 예산지원신청을 완전히 다 처리하고 나면 자동적으로 회계처리내역에 추가되도록 하기 위해서, 회계내역 엔티티로 변환한다. */
  public BudgetHistory makeHistory() {

    if (this.isComplete() && this.personInCharge != null) {
      return BudgetHistory.builder()
          .title(this.title.getValue())
          .dateUsed(this.dateUsed)
          .details(this.details.getValue())
          .income(new Price(0))
          .outcome(this.outcome)
          .personInCharge(this.personInCharge)
          .personReceived(this.applicationWriter)
          .build();
    } else {
      throw new RuntimeException("회계기록 중에 오류가 발생하였습니다!");
    }
  }
}
