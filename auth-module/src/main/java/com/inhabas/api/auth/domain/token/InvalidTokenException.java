package com.inhabas.api.auth.domain.token;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    private static final String defaultMessage = "Invalid token! corrupted signature or expired token";

    public InvalidTokenException() {
        super(defaultMessage);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
