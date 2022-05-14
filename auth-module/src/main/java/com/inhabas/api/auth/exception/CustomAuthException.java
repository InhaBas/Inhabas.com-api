package com.inhabas.api.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * {@code auth-module} 에서 발생하는 오류들은 {@code CustomAuthException} 을 상속받아서 구현해야함.
 * 특히 spring security filter에서 발생하는 오류들을 처리하기 위해서는 필수적으로 {@code AuthenticationException} 을 상속받아야함.
 */
public abstract class CustomAuthException extends AuthenticationException {

    private final String exceptionCode;

    /**
     * @param exceptionCode the {@link AuthExceptionCodes Authentication Exception Codes}
     */
    public CustomAuthException(String exceptionCode) {
        super(exceptionCode);
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }
}
