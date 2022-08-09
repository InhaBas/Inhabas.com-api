package com.inhabas.api.auth.domain.exception;

public class InvalidUserInfoException extends CustomAuthException {

    public InvalidUserInfoException() {
        super(AuthExceptionCodes.INVALID_USER_INFO);
    }
}
