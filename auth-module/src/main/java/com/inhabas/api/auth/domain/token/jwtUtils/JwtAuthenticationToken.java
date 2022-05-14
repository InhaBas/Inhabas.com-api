package com.inhabas.api.auth.domain.token.jwtUtils;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Integer authenticatedMember;

    public JwtAuthenticationToken(Integer authenticatedMember, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.authenticatedMember = authenticatedMember;
    }

    @Deprecated
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authenticatedMember;
    }
}
