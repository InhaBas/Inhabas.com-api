package com.inhabas.api.auth.domain.oauth2.userInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.inhabas.api.auth.domain.error.authException.UnsupportedOAuth2ProviderException;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OAuth2UserInfoFactoryTest {

  @DisplayName("registrationId가 google 이면 GoogleOAuth2UserInfo를 반환한다.")
  @Test
  public void createGoogleUserInfoTest() {
    // given
    Map<String, Object> attributes = Map.of("sub", "1234567891011121314");

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("google", attributes);

    // then
    assertThat(userInfo).isInstanceOf(GoogleOAuth2UserInfo.class);
    assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
  }

  @DisplayName("registrationId는 대소문자를 구분하지 않는다.")
  @Test
  public void registrationIdIsCaseInsensitiveTest() {
    // given
    Map<String, Object> attributes = Map.of("sub", "1234567891011121314");

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("GOOGLE", attributes);

    // then
    assertThat(userInfo).isInstanceOf(GoogleOAuth2UserInfo.class);
  }

  @DisplayName("registrationId가 naver 이면 NaverOAuth2UserInfo를 반환한다.")
  @Test
  public void createNaverUserInfoTest() {
    // given
    Map<String, Object> attributes = Map.of("response", Map.of("id", "12345"));

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("naver", attributes);

    // then
    assertThat(userInfo).isInstanceOf(NaverOAuth2UserInfo.class);
    assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.NAVER);
  }

  @DisplayName("registrationId가 kakao 이면 KakaoOAuth2UserInfo를 반환한다.")
  @Test
  public void createKakaoUserInfoTest() {
    // given
    Map<String, Object> attributes = Map.of("id", 1234567890L);

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("kakao", attributes);

    // then
    assertThat(userInfo).isInstanceOf(KakaoOAuth2UserInfo.class);
    assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.KAKAO);
  }

  @DisplayName("지원하지 않는 registrationId 는 UnsupportedOAuth2ProviderException 을 던진다.")
  @Test
  public void unsupportedProviderTest() {
    // given
    Map<String, Object> attributes = Map.of("id", "1234");

    // when then
    assertThrows(
        UnsupportedOAuth2ProviderException.class,
        () -> OAuth2UserInfoFactory.getOAuth2UserInfo("github", attributes));
  }

  @DisplayName("OAuth2AuthenticationToken 으로부터 registrationId와 속성을 추출하여 UserInfo를 만든다.")
  @Test
  public void createUserInfoFromAuthenticationTokenTest() {
    // given
    Map<String, Object> attributes =
        Map.of("sub", "1234567891011121314", "name", "유동현", "email", "my@gmail.com");
    DefaultOAuth2User principal =
        new DefaultOAuth2User(
            List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")), attributes, "sub");
    OAuth2AuthenticationToken authenticationToken =
        new OAuth2AuthenticationToken(principal, principal.getAuthorities(), "google");

    // when
    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authenticationToken);

    // then
    assertThat(userInfo).isInstanceOf(GoogleOAuth2UserInfo.class);
    assertThat(userInfo.getId()).isEqualTo("1234567891011121314");
    assertThat(userInfo.getEmail()).isEqualTo("my@gmail.com");
  }
}
