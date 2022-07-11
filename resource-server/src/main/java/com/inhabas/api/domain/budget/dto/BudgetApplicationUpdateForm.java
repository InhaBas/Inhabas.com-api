package com.inhabas.api.domain.budget.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetApplicationUpdateForm extends BudgetApplicationRegisterForm {

    @NotNull
    @Positive
    private Integer applicationId;

    public BudgetApplicationUpdateForm(String title, LocalDateTime dateUsed, String details, Integer outcome,
                                       String accounts, Integer applicationId) {

        super(title, dateUsed, details, outcome, accounts);
        this.applicationId = applicationId;
    }
}
