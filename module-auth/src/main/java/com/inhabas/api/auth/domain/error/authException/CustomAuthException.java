package com.inhabas.api.auth.domain.error.authException;

import lombok.Getter;

import org.springframework.security.core.AuthenticationException;

import com.inhabas.api.auth.domain.error.ErrorCode;

/**
 * {@code auth-module} 에서 발생하는 오류들은 {@code CustomAuthException} 을 상속받아서 구현해야함. 특히 spring security
 * filter에서 발생하는 오류들을 처리하기 위해서는 필수적으로 {@code AuthenticationException} 을 상속받아야함.
 */
@Getter
public abstract class CustomAuthException extends AuthenticationException {

  private final ErrorCode errorCode;

  public CustomAuthException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public CustomAuthException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
