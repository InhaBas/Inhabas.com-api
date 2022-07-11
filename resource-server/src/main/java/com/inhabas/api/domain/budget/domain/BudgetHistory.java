package com.inhabas.api.domain.budget.domain;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.budget.BudgetHistoryNotFoundException;
import com.inhabas.api.domain.budget.HistoryCannotModifiableException;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "budget_history")
public class BudgetHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer income;

    private Integer outcome;

    @Column(name = "date_used", nullable = false)
    private LocalDateTime dateUsed;

    private String title;

    private String details;

    private String account;

//    private List<File> receipts;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "person_in_charge", nullable = false))
    private MemberId personInCharge;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "person_received", nullable = false))
    private MemberId personReceived;

    public MemberId getPersonReceived() {
        return personReceived;
    }

    public MemberId getPersonInCharge() {
        return personInCharge;
    }

    public boolean cannotModifiableBy(MemberId CFO) {
        return !this.personInCharge.equals(CFO);
    }

    @Builder
    public BudgetHistory(Integer income, Integer outcome, LocalDateTime dateUsed,
            String title, String details, MemberId personInCharge, MemberId personReceived) {
        this.income = income;
        this.outcome = outcome;
        this.dateUsed = dateUsed;
        this.title = title;
        this.details = details;
        this.personInCharge = personInCharge;
        this.personReceived = personReceived;
    }

    public void modify(MemberId currentCFO, Integer income, Integer outcome, LocalDateTime dateUsed,
            String title, String details, MemberId personReceived) {

        if (this.id == null) {
            throw new BudgetHistoryNotFoundException("cannot modify this entity, because not persisted ever!");
        }

        if (this.cannotModifiableBy(currentCFO))
            throw new HistoryCannotModifiableException();

        this.title = title;
        this.details = details;
        this.dateUsed = dateUsed;
        this.income = income;
        this.outcome = outcome;
        this.personReceived = personReceived;
    }
}
