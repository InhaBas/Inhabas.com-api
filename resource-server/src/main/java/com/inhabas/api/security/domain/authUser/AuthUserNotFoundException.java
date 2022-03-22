package com.inhabas.api.security.domain.authUser;

public class AuthUserNotFoundException extends RuntimeException {

    private static final String defaultMessage = "Cannot found AuthUser.class instance !!";

    public AuthUserNotFoundException() {
        super(defaultMessage);
    }

    public AuthUserNotFoundException(String message) {
        super(message);
    }
}
