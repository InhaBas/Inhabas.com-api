package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationResult extends OAuth2UserInfoAuthentication {

    public JwtAuthenticationResult(String uid, String provider, String email, Collection<? extends GrantedAuthority> authorities) {
        super(uid, provider, email, authorities);
    }

}
