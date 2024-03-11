package com.inhabas.api.domain.budget.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class InvalidIncomeOrOutcomeException extends BusinessException {
  public InvalidIncomeOrOutcomeException() {
    super(ErrorCode.INVALID_INCOME_OR_OUTCOME);
  }
}
