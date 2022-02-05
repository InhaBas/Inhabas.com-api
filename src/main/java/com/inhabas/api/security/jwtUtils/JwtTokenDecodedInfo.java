package com.inhabas.api.security.jwtUtils;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Set;

public class JwtTokenDecodedInfo {

    private final Integer authUserId;
    private final Set<GrantedAuthority> grantedAuthorities;

    public JwtTokenDecodedInfo(Integer authUserId, Set<GrantedAuthority> grantedAuthorities) {
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
