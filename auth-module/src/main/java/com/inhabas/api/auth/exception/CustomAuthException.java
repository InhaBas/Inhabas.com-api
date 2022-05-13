package com.inhabas.api.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthException extends AuthenticationException {

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
