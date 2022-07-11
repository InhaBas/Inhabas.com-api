package com.inhabas.api.domain.budget.domain;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.budget.ApplicationCannotModifiableException;
import com.inhabas.api.domain.budget.ApplicationNotFoundException;
import com.inhabas.api.domain.budget.converter.StatusConverter;
import com.inhabas.api.domain.budget.domain.valueObject.*;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private MemberId applicationWriter;

    @AttributeOverride(name = "id", column = @Column(name = "person_in_charge"))
    private MemberId personInCharge;

    @Convert(converter = StatusConverter.class)
    @Column(nullable = false)
    private ApplicationStatus status;

    private RejectReason rejectReason;

    //private List<File> receipts;


    @Builder
    public BudgetSupportApplication(String title, LocalDateTime dateUsed, String details, Integer outcome,
                                    String account, MemberId applicationWriter) {
        this.title = new Title(title);
        this.dateUsed = dateUsed;
        this.details = new Details(details);
        this.outcome = new Price(outcome);
        this.applicantAccount = new ApplicantAccount(account);
        this.applicationWriter = applicationWriter;
        this.status = ApplicationStatus.WAITING;
    }

    public void modify(String title, LocalDateTime dateUsed, String details, Integer outcome, String account, MemberId currentApplicant) {

        if (this.id == null)
            throw new ApplicationNotFoundException("cannot modify this entity, because not persisted ever!");

        if (this.cannotModifiableBy(currentApplicant))
            throw new ApplicationCannotModifiableException();

        this.title = new Title(title);
        this.dateUsed = dateUsed;
        this.details = new Details(details);
        this.outcome = new Price(outcome);
        this.applicantAccount = new ApplicantAccount(account);
    }

    public boolean cannotModifiableBy(MemberId currentApplicant) {
        return !this.applicationWriter.equals(currentApplicant);
    }
}
