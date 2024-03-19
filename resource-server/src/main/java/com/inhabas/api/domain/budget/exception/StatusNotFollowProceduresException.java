package com.inhabas.api.domain.budget.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class StatusNotFollowProceduresException extends BusinessException {
  public StatusNotFollowProceduresException() {
    super(ErrorCode.STATUS_NOT_FOLLOW_PROCEDURES);
  }
}
