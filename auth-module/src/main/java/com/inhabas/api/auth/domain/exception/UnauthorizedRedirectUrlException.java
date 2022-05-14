package com.inhabas.api.auth.domain.exception;

public class UnauthorizedRedirectUrlException extends CustomAuthException{

    public UnauthorizedRedirectUrlException() {
        super(AuthExceptionCodes.UNAUTHORIZED_REDIRECT_URI);
    }
}
