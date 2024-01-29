package com.inhabas.api.domain.budget.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.budget.BudgetHistoryNotFoundException;
import com.inhabas.api.domain.budget.HistoryCannotModifiableException;
import com.inhabas.api.domain.budget.domain.valueObject.Account;
import com.inhabas.api.domain.budget.domain.valueObject.Details;
import com.inhabas.api.domain.budget.domain.valueObject.Price;
import com.inhabas.api.domain.budget.domain.valueObject.Title;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "budget_history")
public class BudgetHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @AttributeOverride(name = "value", column = @Column(name = "income", nullable = false))
  private Price income;

  @AttributeOverride(name = "value", column = @Column(name = "outcome", nullable = false))
  private Price outcome;

  @Column(name = "date_used", nullable = false)
  private LocalDateTime dateUsed;

  private Title title;

  private Details details;

  private Account account;

  //    private List<File> receipts;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "person_in_charge", nullable = false))
  private StudentId personInCharge;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "person_received", nullable = false))
  private StudentId personReceived;

  public StudentId getPersonReceived() {
    return personReceived;
  }

  public StudentId getPersonInCharge() {
    return personInCharge;
  }

  public boolean cannotModifiableBy(StudentId CFO) {
    return !this.personInCharge.equals(CFO);
  }

  @Builder
  public BudgetHistory(
      Integer income,
      Integer outcome,
      LocalDateTime dateUsed,
      String title,
      String details,
      StudentId personInCharge,
      StudentId personReceived) {
    this.income = new Price(income);
    this.outcome = new Price(outcome);
    this.dateUsed = dateUsed;
    this.title = new Title(title);
    this.details = new Details(details);
    this.personInCharge = personInCharge;
    this.personReceived = personReceived;
  }

  public void modify(
      StudentId currentCFO,
      Integer income,
      Integer outcome,
      LocalDateTime dateUsed,
      String title,
      String details,
      StudentId personReceived) {

    if (this.id == null) {
      throw new BudgetHistoryNotFoundException(
          "cannot modify this entity, because not persisted ever!");
    }

    if (this.cannotModifiableBy(currentCFO)) throw new HistoryCannotModifiableException();

    this.title = new Title(title);
    this.details = new Details(details);
    this.dateUsed = dateUsed;
    this.income = new Price(income);
    this.outcome = new Price(outcome);
    this.personReceived = personReceived;
  }
}
