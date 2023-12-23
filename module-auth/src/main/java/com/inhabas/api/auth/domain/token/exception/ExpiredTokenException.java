package com.inhabas.api.auth.domain.token.exception;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.auth.domain.error.authException.CustomAuthException;


public class ExpiredTokenException extends CustomAuthException {

    public ExpiredTokenException() {
        super(ErrorCode.JWT_EXPIRED);
    }

}
