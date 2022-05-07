package com.inhabas.api.auth.utils.argumentResolver;

import com.inhabas.api.auth.domain.AuthenticationResult;
import lombok.Getter;


public class ResolvedAuthenticationResult extends AuthenticationResult {


    protected @Getter String email;

    protected @Getter String provider;

    protected @Getter Integer memberSocialAccountId;

    protected @Getter boolean isJoined;
}
