package com.inhabas.api.domain.signUp.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class InvalidAnswerLengthException extends BusinessException {

  public InvalidAnswerLengthException() {
    super(ErrorCode.INVALID_ANSWER_LENGTH);
  }
}
