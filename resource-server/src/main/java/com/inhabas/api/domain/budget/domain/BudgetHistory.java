package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.board.exception.OnlyWriterUpdateException;
import com.inhabas.api.domain.budget.domain.valueObject.Account;
import com.inhabas.api.domain.budget.domain.valueObject.Details;
import com.inhabas.api.domain.budget.domain.valueObject.Price;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "BUDGET_HISTORY")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("HISTORY")
public class BudgetHistory extends BudgetBoard {

  @AttributeOverride(name = "value", column = @Column(name = "INCOME", nullable = false))
  private Price income;

  @AttributeOverride(name = "value", column = @Column(name = "OUTCOME", nullable = false))
  private Price outcome;

  @Embedded private Account account;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "USER_ID_IN_CHARGE",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_IN_CHARGE"))
  private Member memberInCharge;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "USER_ID_RECEIVED",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_RECEIVED"))
  private Member memberReceived;

  public boolean cannotModifiableBy(Member secretary) {
    return !this.memberInCharge.equals(secretary);
  }

  public Integer getIncome() {
    return income.getValue();
  }

  public Integer getOutcome() {
    return outcome.getValue();
  }

  public String getAccount() {
    return account == null ? null : account.getValue();
  }

  public Member getMemberInCharge() {
    return memberInCharge;
  }

  public Member getMemberReceived() {
    return memberReceived;
  }

  @Builder
  public BudgetHistory(
      Price income,
      Price outcome,
      String title,
      String details,
      Account account,
      Member writer,
      LocalDateTime dateUsed,
      Member memberInCharge,
      Member memberReceived) {
    super(title, details, dateUsed, writer);
    this.income = income;
    this.outcome = outcome;
    this.account = account == null ? new Account("") : account;
    this.memberInCharge = memberInCharge;
    this.memberReceived = memberReceived;
  }

  public void modify(
      Member secretary,
      Integer income,
      Integer outcome,
      LocalDateTime dateUsed,
      String title,
      String details,
      Member personReceived) {

    if (this.id == null) {
      throw new NotFoundException();
    }

    if (this.cannotModifiableBy(secretary)) {
      throw new OnlyWriterUpdateException();
    }

    this.title = new Title(title);
    this.details = new Details(details);
    this.dateUsed = dateUsed;
    this.income = new Price(income);
    this.outcome = new Price(outcome);
    this.memberReceived = personReceived;
  }
}
