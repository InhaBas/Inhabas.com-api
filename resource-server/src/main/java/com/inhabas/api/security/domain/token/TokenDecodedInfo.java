package com.inhabas.api.security.domain.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Set;

public abstract class TokenDecodedInfo {

    private final Integer authUserId;
    private final Set<GrantedAuthority> grantedAuthorities;

    public TokenDecodedInfo(Integer authUserId, Set<GrantedAuthority> grantedAuthorities) {
        this.authUserId = authUserId;
        this.grantedAuthorities = grantedAuthorities;
    }

    public Integer getAuthUserId() {
        return authUserId;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return Collections.unmodifiableSet(grantedAuthorities);
    }
}
