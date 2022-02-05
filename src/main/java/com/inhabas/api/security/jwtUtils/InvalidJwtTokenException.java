package com.inhabas.api.security.jwtUtils;

public class InvalidJwtTokenException extends RuntimeException {

    private static final String defaultMessage = "Invalid jwt token! Maybe jwt token signature had been corrupted!";

    public InvalidJwtTokenException() {
        super(defaultMessage);
    }

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
