package com.inhabas.api.auth.exception;

public class InvalidUserInfoException extends CustomAuthException {

    public InvalidUserInfoException() {
        super(AuthExceptionCodes.INVALID_USER_INFO);
    }
}
