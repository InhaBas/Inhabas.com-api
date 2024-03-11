package com.inhabas.api.domain.budget.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class InProcessModifiableException extends BusinessException {
  public InProcessModifiableException() {
    super(ErrorCode.IN_PROCESS_UNMODIFIABLE);
  }
}
