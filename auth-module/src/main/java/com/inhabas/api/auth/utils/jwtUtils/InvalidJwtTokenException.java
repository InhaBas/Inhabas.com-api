package com.inhabas.api.auth.utils.jwtUtils;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtTokenException extends AuthenticationException {

    private static final String defaultMessage = "Invalid jwt token! corrupted signature or expired token";

    public InvalidJwtTokenException() {
        super(defaultMessage);
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
