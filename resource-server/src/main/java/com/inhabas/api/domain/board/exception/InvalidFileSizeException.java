package com.inhabas.api.domain.board.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class InvalidFileSizeException extends BusinessException {

  public InvalidFileSizeException() {
    super(ErrorCode.INVALID_FILE_SIZE);
  }
}
