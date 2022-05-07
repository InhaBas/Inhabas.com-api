package com.inhabas.api.auth.domain.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Set;

public abstract class TokenDecodedInfo {

    private final Integer memberId;
    private final Integer memberSocialAccountId;
    private final Set<GrantedAuthority> grantedAuthorities;

    public TokenDecodedInfo(Integer authUserId, Integer muId, Set<GrantedAuthority> grantedAuthorities) {
        this.memberId = authUserId;
        this.grantedAuthorities = grantedAuthorities;
        this.memberSocialAccountId = muId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public Integer getMemberSocialAccountId() {
        return memberSocialAccountId;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return Collections.unmodifiableSet(grantedAuthorities);
    }
}
