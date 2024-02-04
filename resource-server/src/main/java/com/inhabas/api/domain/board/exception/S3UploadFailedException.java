package com.inhabas.api.domain.board.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.businessException.BusinessException;

public class S3UploadFailedException extends BusinessException {

  public S3UploadFailedException() {
    super(ErrorCode.S3_UPLOAD_FAILED);
  }
}
