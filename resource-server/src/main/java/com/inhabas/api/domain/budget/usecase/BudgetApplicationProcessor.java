package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;

public interface BudgetApplicationProcessor {

    void process(Integer applicationId, BudgetApplicationStatusChangeRequest request, MemberId inCharge);
}
