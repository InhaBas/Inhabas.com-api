package com.inhabas.api.domain.budget.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetApplicationStatusChangeRequest {

  @NotNull private ApplicationStatus status;

  private String rejectReason;

  public BudgetApplicationStatusChangeRequest(String status, String rejectReason) {
    this.status = ApplicationStatus.valueOf(status);
    this.rejectReason = rejectReason;
  }
}
