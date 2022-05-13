package com.inhabas.api.auth.exception;

public class UnauthorizedRedirectUrlException extends CustomAuthException{

    public UnauthorizedRedirectUrlException() {
        super(AuthExceptionCodes.UNAUTHORIZED_REDIRECT_URI);
    }
}
