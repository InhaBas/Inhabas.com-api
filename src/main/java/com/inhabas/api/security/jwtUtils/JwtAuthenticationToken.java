package com.inhabas.api.security.jwtUtils;

import com.inhabas.api.security.domain.AuthUserDetail;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUserDetail principal;

    public JwtAuthenticationToken(AuthUserDetail authUser, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = authUser;
    }

    @Deprecated
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
