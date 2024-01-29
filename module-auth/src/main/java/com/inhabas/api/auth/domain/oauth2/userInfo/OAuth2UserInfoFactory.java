package com.inhabas.api.auth.domain.oauth2.userInfo;

import java.util.Map;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.inhabas.api.auth.domain.error.authException.UnsupportedOAuth2ProviderException;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;

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

  static OAuth2UserInfo getOAuth2UserInfo(OAuth2AuthenticationToken auth2AuthenticationToken) {
    return OAuth2UserInfoFactory.getOAuth2UserInfo(
        auth2AuthenticationToken.getAuthorizedClientRegistrationId(),
        auth2AuthenticationToken.getPrincipal().getAttributes());
  }
}
