package com.inhabas.api.auth.domain.token.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.authException.CustomAuthException;

public class TokenMissingException extends CustomAuthException {

    public TokenMissingException() {
        super(ErrorCode.JWT_MISSING);
    }

}