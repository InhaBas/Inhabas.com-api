package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.budget.domain.valueObject.*;
import com.inhabas.api.domain.budget.exception.ApplicationCannotModifiableException;
import com.inhabas.api.domain.menu.domain.Menu;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BUDGET_SUPPORT_APPLICATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("BUDGET_APPLICATION")
public class BudgetSupportApplication extends BaseBoard {

  @Embedded private Details details;

  @Column(nullable = false, columnDefinition = "DATETIME(0)")
  private LocalDateTime dateUsed;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "USER_IN_CHARGE_ID",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_APPLICATION"))
  private Member memberInCharge;

  @Embedded private Account account;

  @AttributeOverride(name = "value", column = @Column(name = "OUTCOME", nullable = false))
  private Price outcome;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "USER_APPLICANT_ID",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_APPLICANT"))
  private Member applicant;

  @Column(nullable = false)
  private RequestStatus status;

  private RejectReason rejectReason;

  @Column(name = "DATE_CHECKED")
  private LocalDateTime dateChecked;

  @Builder
  public BudgetSupportApplication(
      Menu menu,
      String title,
      String details,
      LocalDateTime dateUsed,
      String account,
      Integer outcome,
      Member applicant,
      RequestStatus status) {
    super(title, menu);
    this.details = new Details(details);
    this.dateUsed = dateUsed;
    this.account = new Account(account);
    this.outcome = new Price(outcome);
    this.applicant = applicant;
    this.status = status;
  }

  public void modify(
      String title,
      LocalDateTime dateUsed,
      String details,
      Integer outcome,
      String account,
      Member applicant) {

    if (this.id == null) {
      throw new NotFoundException();
    }

    if (this.cannotModifiableBy(applicant)) throw new ApplicationCannotModifiableException();

    this.title = new Title(title);
    this.dateUsed = dateUsed;
    this.details = new Details(details);
    this.outcome = new Price(outcome);
    this.account = new Account(account);
    this.status = RequestStatus.PENDING;
  }

  public boolean cannotModifiableBy(Member currentApplicant) {
    return !this.applicant.equals(currentApplicant) || !this.status.equals(RequestStatus.PENDING);
  }

  public void approve(Member memberInCharge) {

    this.status = RequestStatus.APPROVED;
    this.memberInCharge = memberInCharge;
  }

  public void pending(Member memberInCharge) {

    this.status = RequestStatus.PENDING;
    this.memberInCharge = memberInCharge;
  }

  public void reject(String reason, Member memberInCharge) {

    this.rejectReason = new RejectReason(reason);
    this.status = RequestStatus.REJECTED;
    this.memberInCharge = memberInCharge;
  }

  public void complete(Member memberInCharge) {

    if (this.isComplete()) throw new ApplicationCannotModifiableException("이미 처리가 완료된 예산지원 내역입니다.");

    this.status = RequestStatus.COMPLETED;
    this.memberInCharge = memberInCharge;
  }

  private boolean isComplete() {
    return this.status == RequestStatus.COMPLETED;
  }

  /** 예산지원신청을 완전히 다 처리하고 나면 자동적으로 회계처리내역에 추가되도록 하기 위해서, 회계내역 엔티티로 변환한다. */
  public BudgetHistory makeHistory() {

    if (this.isComplete() && this.memberInCharge != null) {
      return BudgetHistory.builder()
          .title(this.title.getValue())
          .dateUsed(this.dateUsed)
          .details(this.details.getValue())
          .income(0)
          .outcome(this.outcome.getValue())
          .memberInCharge(this.memberInCharge)
          .memberReceived(this.applicant)
          .build();
    } else {
      throw new RuntimeException("회계기록 중에 오류가 발생하였습니다!");
    }
  }
}
