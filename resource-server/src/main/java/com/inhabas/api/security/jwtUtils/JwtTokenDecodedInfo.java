package com.inhabas.api.security.jwtUtils;

import org.springframework.security.core.GrantedAuthority;
import java.util.Set;

public class JwtTokenDecodedInfo extends TokenDecodedInfo {

    public JwtTokenDecodedInfo(Integer authUserId, Set<GrantedAuthority> grantedAuthorities) {
        super(authUserId, grantedAuthorities);
    }
}
