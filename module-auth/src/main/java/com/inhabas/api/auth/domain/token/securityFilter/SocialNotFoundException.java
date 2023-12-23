package com.inhabas.api.auth.domain.token.securityFilter;

import com.inhabas.api.auth.domain.error.authException.CustomAuthException;
import com.inhabas.api.auth.domain.error.ErrorCode;

public class SocialNotFoundException extends CustomAuthException {

    public SocialNotFoundException() {
        super(ErrorCode.SOCIAL_NOT_FOUND);
    }
}
