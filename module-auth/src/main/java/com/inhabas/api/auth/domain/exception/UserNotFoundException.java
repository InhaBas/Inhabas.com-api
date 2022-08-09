package com.inhabas.api.auth.domain.exception;

public class UserNotFoundException extends CustomAuthException {

    public UserNotFoundException() {
        super(AuthExceptionCodes.USER_NOT_FOUND);
    }
}
