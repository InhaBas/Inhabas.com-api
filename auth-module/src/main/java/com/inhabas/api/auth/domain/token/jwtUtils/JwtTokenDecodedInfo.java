package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.token.TokenDecodedInfo;
import org.springframework.security.core.GrantedAuthority;
import java.util.Set;

public class JwtTokenDecodedInfo extends TokenDecodedInfo {

    public JwtTokenDecodedInfo(Integer memberId, Integer muId, Set<GrantedAuthority> grantedAuthorities) {
        super(memberId, muId, grantedAuthorities);
    }
}
