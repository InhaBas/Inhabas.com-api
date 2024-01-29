package com.inhabas.api.auth.domain.oauth2;

import java.util.Arrays;

import com.inhabas.api.auth.domain.error.authException.UnsupportedOAuth2ProviderException;

public enum OAuth2Provider {
  GOOGLE,
  NAVER,
  KAKAO;

  /**
   * 대소문자 구분하지 않고 OAuth2Provider 타입으로 변환.
   *
   * @param registrationId oauth2 provider
   * @return OAuth2Provider
   * @exception UnsupportedOAuth2ProviderException 지원하지 않는 OAuth2 Provider 입니다.
   */
  public static OAuth2Provider convert(String registrationId) {
    return Arrays.stream(OAuth2Provider.values())
        .filter(provider -> provider.toString().equals(registrationId.toUpperCase()))
        .findAny()
        .orElseThrow(UnsupportedOAuth2ProviderException::new);
  }
}
