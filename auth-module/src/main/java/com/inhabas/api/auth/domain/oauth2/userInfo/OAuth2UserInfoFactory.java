package com.inhabas.api.auth.domain.oauth2.userInfo;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.exception.UnsupportedOAuth2ProviderException;

import java.util.Map;

public interface OAuth2UserInfoFactory {
    static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {

        OAuth2UserInfo userInfo = null;
        OAuth2Provider oAuth2Provider = OAuth2Provider.convert(registrationId);
        switch (oAuth2Provider) {
            case GOOGLE:
                userInfo = new GoogleOAuth2UserInfo(attributes);
                break;
            case NAVER:
                userInfo = new NaverOAuth2UserInfo(attributes);
                break;
            case KAKAO:
                userInfo = new KakaoOAuth2UserInfo(attributes);
                break;
            default:
                throw new UnsupportedOAuth2ProviderException();
        }

        return userInfo;
    }
}
