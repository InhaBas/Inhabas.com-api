package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;

public interface BudgetApplicationProcessor {

  void process(Long applicationId, BudgetApplicationStatusChangeRequest request, Long inCharge);
}
