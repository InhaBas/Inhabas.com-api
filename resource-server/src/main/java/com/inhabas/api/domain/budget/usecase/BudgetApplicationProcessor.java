package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;

public interface BudgetApplicationProcessor {

    void process(Integer applicationId, BudgetApplicationStatusChangeRequest request, StudentId inCharge);
}
