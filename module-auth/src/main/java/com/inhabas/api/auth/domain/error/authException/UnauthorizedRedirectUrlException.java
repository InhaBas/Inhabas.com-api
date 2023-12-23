package com.inhabas.api.auth.domain.error.authException;

import com.inhabas.api.auth.domain.error.ErrorCode;

public class UnauthorizedRedirectUrlException extends CustomAuthException{

    public UnauthorizedRedirectUrlException() {
        super(ErrorCode.UNAUTHORIZED_REDIRECT_URI);
    }
}
