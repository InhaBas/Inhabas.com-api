package com.inhabas.api.auth.domain.exception;

public class BadRequestException extends CustomAuthException {

    public BadRequestException(String exceptionCode) {
        super(exceptionCode);
    }
}
