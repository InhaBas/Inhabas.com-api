package com.inhabas.api.security.utils.argumentResolver;

import com.inhabas.api.security.domain.AuthenticationResult;
import lombok.Getter;


public class ResolvedAuthenticationResult extends AuthenticationResult {


    protected @Getter String email;

    protected @Getter String provider;

    protected @Getter Integer memberSocialAccountId;

    protected @Getter boolean isJoined;
}
