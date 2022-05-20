package com.inhabas.api.domain.member.security;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalNotFoundException;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationResult;
import com.inhabas.api.domain.member.security.socialAccount.MemberSocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberPrincipalService implements UserPrincipalService {

    private final MemberSocialAccountRepository memberSocialAccountRepository;

    @Override
    public Object loadUserPrincipal(Authentication authentication) {
        JwtAuthenticationResult jwtAuthenticationResult = (JwtAuthenticationResult) authentication;
        String provider = jwtAuthenticationResult.getProvider();
        String uid = jwtAuthenticationResult.getUid();

        return memberSocialAccountRepository.findMemberIdByUidAndProvider(new UID(uid), OAuth2Provider.convert(provider))
                .orElseThrow(() -> { throw new UserPrincipalNotFoundException(); });
    }
}
