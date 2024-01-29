package com.inhabas.api.auth.domain.error.businessException;

import lombok.Getter;

import com.inhabas.api.auth.domain.error.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;

  public BusinessException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
