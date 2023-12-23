package com.inhabas.api.auth.domain.error.authException;

import com.inhabas.api.auth.domain.error.ErrorCode;

public class InvalidOAuth2InfoException extends CustomAuthException {

    public InvalidOAuth2InfoException() {
        super(ErrorCode.INVALID_OAUTH2_INFO);
    }
}
