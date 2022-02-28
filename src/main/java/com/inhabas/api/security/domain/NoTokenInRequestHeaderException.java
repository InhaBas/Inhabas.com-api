package com.inhabas.api.security.domain;

public class NoTokenInRequestHeaderException extends RuntimeException {
    private static final String defaultMessage = "no token in request header!!";

    public NoTokenInRequestHeaderException() {
        super(defaultMessage);
    }

    public NoTokenInRequestHeaderException(String message) {
        super(message);
    }
}
