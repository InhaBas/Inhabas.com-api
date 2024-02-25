package com.inhabas.api.domain.budget.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetApplicationStatusChangeRequest {

  @NotNull private RequestStatus status;

  private String rejectReason;

  public BudgetApplicationStatusChangeRequest(String status, String rejectReason) {
    this.status = RequestStatus.valueOf(status);
    this.rejectReason = rejectReason;
  }
}
