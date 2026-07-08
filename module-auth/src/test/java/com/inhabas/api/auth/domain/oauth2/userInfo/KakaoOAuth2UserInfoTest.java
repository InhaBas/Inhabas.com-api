package com.inhabas.api.auth.domain.oauth2.userInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class KakaoOAuth2UserInfoTest {

  private static Map<String, Object> kakaoAttributes() {
    Map<String, Object> profile = new HashMap<>();
    profile.put("nickname", "동현 유");
    profile.put("thumbnail_image_url", "http://k.kakaocdn.net/dn/img_110x110.jpg");
    profile.put("profile_image_url", "http://k.kakaocdn.net/dn/img_640x640.jpg");
    profile.put("is_default_image", false);

    Map<String, Object> kakaoAccount = new HashMap<>();
    kakaoAccount.put("profile", profile);
    kakaoAccount.put("has_email", true);
    kakaoAccount.put("email", "my@gmail.com");

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("id", 1234567890L);
    attributes.put("connected_at", "2022-01-28T14:26:21Z");
    attributes.put("kakao_account", kakaoAccount);
    return attributes;
  }

  @DisplayName("카카오 attributes 에서 id, name, email, imageUrl 을 읽어온다.")
  @Test
  public void getFieldsTest() {
    // given
    KakaoOAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(kakaoAttributes());

    // then
    assertThat(userInfo.getId()).isEqualTo("1234567890"); // 숫자 id 도 문자열로 변환된다.
    assertThat(userInfo.getName()).isEqualTo("동현 유");
    assertThat(userInfo.getEmail()).isEqualTo("my@gmail.com");
    assertThat(userInfo.getImageUrl()).isEqualTo("http://k.kakaocdn.net/dn/img_640x640.jpg");
    assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.KAKAO);
  }

  @DisplayName("getExtraData 는 kakao_account 맵을 반환한다.")
  @Test
  public void getExtraDataTest() {
    // given
    Map<String, Object> attributes = kakaoAttributes();
    KakaoOAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(attributes);

    // when then
    assertThat(userInfo.getExtraData()).isEqualTo(attributes.get("kakao_account"));
  }

  @DisplayName("getAttributes 가 반환하는 map 은 수정할 수 없다.")
  @Test
  public void attributesAreUnmodifiableTest() {
    // given
    KakaoOAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(kakaoAttributes());

    // when then
    assertThrows(
        UnsupportedOperationException.class, () -> userInfo.getAttributes().put("key", "value"));
  }

  @DisplayName("필수값이 모두 존재하면 validateNecessaryFields 는 true 를 반환한다.")
  @Test
  public void validateNecessaryFieldsTest() {
    // given
    KakaoOAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(kakaoAttributes());

    // when then
    assertThat(userInfo.validateNecessaryFields()).isTrue();
  }

  @DisplayName("email 이 없으면 validateNecessaryFields 는 false 를 반환한다.")
  @Test
  public void validateNecessaryFieldsFailTest() {
    // given
    Map<String, Object> attributes = kakaoAttributes();
    ((Map<?, ?>) attributes.get("kakao_account")).remove("email");
    KakaoOAuth2UserInfo userInfo = new KakaoOAuth2UserInfo(attributes);

    // when then
    assertThat(userInfo.validateNecessaryFields()).isFalse();
  }
}
