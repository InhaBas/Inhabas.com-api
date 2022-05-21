package com.inhabas.api.auth.utils.argumentResolver;

import lombok.Getter;


public class ResolvedAuthenticationResult extends AuthenticationResult {


    protected @Getter String email;

    protected @Getter String provider;

    protected @Getter Integer memberSocialAccountId;

    protected @Getter boolean isJoined;
}
