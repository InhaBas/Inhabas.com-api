package com.inhabas.api.auth.domain.oauth2.userAuthorityProvider;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;


public class DefaultUserAuthorityProvider implements UserAuthorityProvider {

    @Override
    public Collection<SimpleGrantedAuthority> determineAuthorities(OAuth2UserInfo oAuth2UserInfo) {

        return Collections.singleton(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
    }
}
