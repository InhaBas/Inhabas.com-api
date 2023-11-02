package com.inhabas.api.auth.domain.token.exception;

import org.springframework.security.core.AuthenticationException;

public class MissingTokenException extends AuthenticationException {

    private static final String defaultMessage = "Authentication token is missing.";

    public MissingTokenException() {
        super(defaultMessage);
    }

    public MissingTokenException(String message) {
        super(message);
    }

}