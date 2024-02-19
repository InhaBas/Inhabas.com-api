package com.inhabas.api.domain.board.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class OnlyWriterUpdateException extends BusinessException {

  public OnlyWriterUpdateException() {
    super(ErrorCode.ONLY_WRITER_UPDATE);
  }
}
