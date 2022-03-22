package com.inhabas.api.security.domain.token;

public class RefreshTokenNotFoundException extends RuntimeException {
    private static final String defaultMessage = "Cannot found such refreshToken!!";

    public RefreshTokenNotFoundException() {
        super(defaultMessage);
    }

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
