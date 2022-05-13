package com.inhabas.api.auth.exception;

public class BadRequestException extends CustomAuthException {

    public BadRequestException(String exceptionCode) {
        super(exceptionCode);
    }
}
