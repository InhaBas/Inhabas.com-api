package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.budget.domain.valueObject.Account;
import com.inhabas.api.domain.budget.domain.valueObject.Details;
import com.inhabas.api.domain.budget.domain.valueObject.Price;
import com.inhabas.api.domain.menu.domain.Menu;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BUDGET_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("BUDGET_HISTORY")
public class BudgetHistory extends BaseBoard {

  @Embedded private Details details;

  @Getter
  @Column(nullable = false, columnDefinition = "DATETIME(0)")
  private LocalDateTime dateUsed;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "IN_CHARGE_USER_ID",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_HISTORY"))
  private Member memberInCharge;

  @Embedded private Account account;

  @AttributeOverride(name = "value", column = @Column(name = "INCOME", nullable = false))
  private Price income;

  @AttributeOverride(name = "value", column = @Column(name = "OUTCOME", nullable = false))
  private Price outcome;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "RECEIVED_USER_ID",
      foreignKey = @ForeignKey(name = "FK_MEMBER_OF_BUDGET_RECEIVED"))
  private Member memberReceived;

  public String getDetails() {
    return details.getValue();
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

  @Builder
  public BudgetHistory(
      String title,
      Menu menu,
      String details,
      LocalDateTime dateUsed,
      Member memberInCharge,
      String account,
      Integer income,
      Integer outcome,
      Member memberReceived) {
    super(title, menu);
    this.details = new Details(details);
    this.dateUsed = dateUsed;
    this.memberInCharge = memberInCharge;
    this.account = new Account(account);
    this.income = new Price(income);
    this.outcome = new Price(outcome);
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

    this.title = new Title(title);
    this.details = new Details(details);
    this.dateUsed = dateUsed;
    this.income = new Price(income);
    this.outcome = new Price(outcome);
    this.memberInCharge = secretary;
    this.memberReceived = personReceived;
  }
}
