package com.inhabas.api.auth.domain.oauth2;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("google", oAuth2User.getAttributes());

        // 필수값 받아왔는지 확인
        if(!oAuth2UserInfo.validateNecessaryFields()) {
            //throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        // db 에 소셜 계정 정보 update



        // 기존 회원인지 확인.


        return oAuth2User;
    }
}
