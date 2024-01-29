package com.inhabas.api.auth.domain.error.authException;

import com.inhabas.api.auth.domain.error.ErrorCode;

public class InvalidAuthorityException extends CustomAuthException {

    public InvalidAuthorityException() {
        super(ErrorCode.AUTHORITY_INVALID);
    }

}
