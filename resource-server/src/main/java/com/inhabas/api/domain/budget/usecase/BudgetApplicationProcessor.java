package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;

public interface BudgetApplicationProcessor {

  void process(
      Integer applicationId, BudgetApplicationStatusChangeRequest request, StudentId inCharge);
}
