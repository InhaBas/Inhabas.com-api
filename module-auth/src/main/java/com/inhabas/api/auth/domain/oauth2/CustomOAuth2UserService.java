package com.inhabas.api.auth.domain.oauth2;

import com.inhabas.api.auth.domain.exception.InvalidUserInfoException;
import com.inhabas.api.auth.domain.oauth2.socialAccount.SocialAccountService;
import com.inhabas.api.auth.domain.oauth2.userAuthorityProvider.UserAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoFactory;
import com.inhabas.api.domain.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialAccountService socialAccountService;
    private final UserAuthorityProvider userAuthorityProvider;
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        // 필수값 받아왔는지 확인
        if(!oAuth2UserInfo.validateNecessaryFields()) {
            throw new InvalidUserInfoException();
        }
        // db 에 소셜 계정 정보 update
        memberService.updateSocialAccountInfo(oAuth2UserInfo);

        // 현재 로그인하려는 유저에 맞는 권한을 들고옴.
        Collection<SimpleGrantedAuthority> authorities = userAuthorityProvider.determineAuthorities(oAuth2UserInfo);

        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        return new DefaultOAuth2User(authorities, oAuth2UserInfo.getAttributes(), nameAttributeKey);
    }
}
