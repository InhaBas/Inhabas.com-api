package com.inhabas.api.domain.budget.domain;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.budget.ApplicationCannotModifiableException;
import com.inhabas.api.domain.budget.ApplicationNotFoundException;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetSupportApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private LocalDateTime dateUsed;

    private String details;

    private Integer outcome;

    private String account;

    @AttributeOverride(name = "id", column = @Column(nullable = false, name = "applicant"))
    private MemberId applicationWriter;

    @AttributeOverride(name = "id", column = @Column(name = "person_in_charge"))
    private MemberId personInCharge;

    @Column(nullable = false) // converter 필요함.
    private ApplicationStatus status;

    private String rejectReason;

    //private List<File> receipts;


    @Builder
    public BudgetSupportApplication(String title, LocalDateTime dateUsed, String details, Integer outcome,
                                    String account, MemberId applicationWriter) {
        this.title = title;
        this.dateUsed = dateUsed;
        this.details = details;
        this.outcome = outcome;
        this.account = account;
        this.applicationWriter = applicationWriter;
        this.status = ApplicationStatus.WAITING;
    }

    public void modify(String title, LocalDateTime dateUsed, String details, Integer outcome, String accounts, MemberId currentApplicant) {

        if (this.id == null)
            throw new ApplicationNotFoundException("cannot modify this entity, because not persisted ever!");

        if (this.cannotModifiableBy(currentApplicant))
            throw new ApplicationCannotModifiableException();

        this.title = title;
        this.details = details;
        this.dateUsed = dateUsed;
        this.outcome = outcome;
        this.account = accounts;
    }

    public boolean cannotModifiableBy(MemberId currentApplicant) {
        return !this.applicationWriter.equals(currentApplicant);
    }
}
