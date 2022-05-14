package com.inhabas.api.domain.member;

import com.inhabas.api.auth.domain.oauth2.authorityService.UserAuthorityService;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class MemberAuthorityProvider implements UserAuthorityProvider {

    private final MemberSocialAccountRepository memberSocialAccountRepository;

    @Override
    public Collection<SimpleGrantedAuthority> determineAuthoritiesAccordingTo(OAuth2UserInfo oAuth2UserInfo) {
        return null;
    }
}
