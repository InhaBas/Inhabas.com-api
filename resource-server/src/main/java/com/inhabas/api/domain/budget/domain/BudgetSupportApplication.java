package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.budget.domain.valueObject.*;
import com.inhabas.api.domain.budget.exception.StatusNotFollowProceduresException;
import com.inhabas.api.domain.menu.domain.Menu;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BUDGET_SUPPORT_APPLICATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("BUDGET_APPLICATION")
public class BudgetSupportApplication extends BaseBoard {

  @Embedded private Details details;

  @Getter
  @Column(nullable = false, columnDefinition = "DATETIME(0)")
  private LocalDateTime dateUsed;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "IN_CHARGE_USER_ID",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_APPLICATION"))
  private Member memberInCharge;

  @Embedded private Account account;

  @Embedded private Price outcome;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "APPLICANT_USER_ID",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_APPLICANT"))
  private Member applicant;

  @Getter
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RequestStatus status;

  @Getter @Embedded private RejectReason rejectReason;

  @Column(name = "DATE_CHECKED", columnDefinition = "DATETIME(0)")
  private LocalDateTime dateChecked;

  @Column(name = "DATE_DEPOSITED", columnDefinition = "DATETIME(0)")
  private LocalDateTime dateDeposited;

  public String getDetails() {
    return details.getValue();
  }

  public Integer getOutcome() {
    return outcome.getValue();
  }

  public String getAccount() {
    return account == null ? null : account.getValue();
  }

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
      String title, LocalDateTime dateUsed, String details, Integer outcome, String account) {

    if (this.id == null) {
      throw new NotFoundException();
    }

    if (!this.isPending()) throw new StatusNotFollowProceduresException();

    this.title = new Title(title);
    this.dateUsed = dateUsed;
    this.details = new Details(details);
    this.outcome = new Price(outcome);
    this.account = new Account(account);
  }

  public boolean isPending() {
    return this.status == RequestStatus.PENDING;
  }

  private boolean isApproved() {
    return this.status.equals(RequestStatus.APPROVED);
  }

  private boolean isComplete() {
    return this.status.equals(RequestStatus.COMPLETED);
  }

  public void pending() {
    if (!this.isPending()) {
      throw new StatusNotFollowProceduresException();
    }
  }

  public void approve(Member memberInCharge) {
    if (this.isPending()) {
      this.status = RequestStatus.APPROVED;
      this.dateChecked = LocalDateTime.now();
      this.memberInCharge = memberInCharge;
    } else {
      throw new StatusNotFollowProceduresException();
    }
  }

  public void reject(String reason, Member memberInCharge) {
    if (this.isPending()) {
      this.rejectReason = new RejectReason(reason);
      this.status = RequestStatus.REJECTED;
      this.dateChecked = LocalDateTime.now();
      this.memberInCharge = memberInCharge;
    } else {
      throw new StatusNotFollowProceduresException();
    }
  }

  public void complete(Member memberInCharge) {
    if (this.isApproved()) {
      this.status = RequestStatus.COMPLETED;
      this.dateDeposited = LocalDateTime.now();
      this.memberInCharge = memberInCharge;
    } else {
      throw new StatusNotFollowProceduresException();
    }
  }

  /** 예산지원신청을 완전히 다 처리하고 나면 자동적으로 회계처리내역에 추가되도록 하기 위해서, 회계내역 엔티티로 변환한다. */
  public BudgetHistory makeHistory(Member secretary, Menu menu) {
    if (this.isComplete() && this.memberInCharge != null) {
      return BudgetHistory.builder()
          .title(this.title.getValue())
          .menu(menu)
          .details(this.details.getValue())
          .dateUsed(this.dateUsed)
          .memberInCharge(this.memberInCharge)
          .account(this.account.getValue())
          .income(0)
          .outcome(this.outcome.getValue())
          .memberReceived(this.applicant)
          .build()
          .writtenBy(secretary, BudgetHistory.class);
    } else {
      throw new StatusNotFollowProceduresException();
    }
  }
}
