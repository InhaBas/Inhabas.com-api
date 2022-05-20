package com.inhabas.api.auth.domain.token.securityFilter;

import com.inhabas.api.auth.domain.exception.AuthExceptionCodes;
import com.inhabas.api.auth.domain.exception.CustomAuthException;

public class UserPrincipalNotFoundException extends CustomAuthException {

    public UserPrincipalNotFoundException() {
        super(AuthExceptionCodes.USER_NOT_FOUND);
    }
}
