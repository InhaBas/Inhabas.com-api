package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.token.TokenAuthenticationResult;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationResult extends TokenAuthenticationResult {

    private final String uid;

    private final String provider;

    public JwtAuthenticationResult(String uid, String provider, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.uid = uid;
        this.provider = provider;
    }

    public String getUid() {
        return uid;
    }

    public String getProvider() {
        return provider;
    }

    @Deprecated
    @Override
    public Object getCredentials() {
        return null;
    }
}
