package com.inhabas.api.domain.board.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class WriterUnmodifiableException extends BusinessException {

  public WriterUnmodifiableException() {
    super(ErrorCode.WRITER_UNMODIFIABLE);
  }
}
