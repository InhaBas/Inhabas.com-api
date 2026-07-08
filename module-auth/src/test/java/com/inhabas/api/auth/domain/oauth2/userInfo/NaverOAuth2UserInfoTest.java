package com.inhabas.api.auth.domain.oauth2.userInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class NaverOAuth2UserInfoTest {

  private static Map<String, Object> naverAttributes() {
    Map<String, Object> response = new HashMap<>();
    response.put("id", "32742776");
    response.put("profile_image", "https://ssl.pstatic.net/static/pwe/address/img_profile.png");
    response.put("gender", "M");
    response.put("email", "my@naver.com");
    response.put("mobile", "010-1234-1234");
    response.put("name", "홍길동");

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("resultcode", "00");
    attributes.put("message", "success");
    attributes.put("response", response);
    return attributes;
  }

  @DisplayName("네이버 attributes 의 response 에서 id, name, email, imageUrl 을 읽어온다.")
  @Test
  public void getFieldsTest() {
    // given
    NaverOAuth2UserInfo userInfo = new NaverOAuth2UserInfo(naverAttributes());

    // then
    assertThat(userInfo.getId()).isEqualTo("32742776");
    assertThat(userInfo.getName()).isEqualTo("홍길동");
    assertThat(userInfo.getEmail()).isEqualTo("my@naver.com");
    assertThat(userInfo.getImageUrl())
        .isEqualTo("https://ssl.pstatic.net/static/pwe/address/img_profile.png");
    assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.NAVER);
  }

  @DisplayName("getExtraData 는 response 맵을 반환한다.")
  @Test
  public void getExtraDataTest() {
    // given
    Map<String, Object> attributes = naverAttributes();
    NaverOAuth2UserInfo userInfo = new NaverOAuth2UserInfo(attributes);

    // when then
    assertThat(userInfo.getExtraData()).isEqualTo(attributes.get("response"));
  }

  @DisplayName("getAttributes 가 반환하는 map 은 수정할 수 없다.")
  @Test
  public void attributesAreUnmodifiableTest() {
    // given
    NaverOAuth2UserInfo userInfo = new NaverOAuth2UserInfo(naverAttributes());

    // when then
    assertThrows(
        UnsupportedOperationException.class, () -> userInfo.getAttributes().put("key", "value"));
  }

  @DisplayName("필수값이 모두 존재하면 validateNecessaryFields 는 true 를 반환한다.")
  @Test
  public void validateNecessaryFieldsTest() {
    // given
    NaverOAuth2UserInfo userInfo = new NaverOAuth2UserInfo(naverAttributes());

    // when then
    assertThat(userInfo.validateNecessaryFields()).isTrue();
  }

  @DisplayName("email, name, profile_image 중 하나라도 없으면 validateNecessaryFields 는 false 를 반환한다.")
  @ParameterizedTest
  @ValueSource(strings = {"email", "name", "profile_image"})
  public void validateNecessaryFieldsFailTest(String missingField) {
    // given
    Map<String, Object> attributes = naverAttributes();
    ((Map<?, ?>) attributes.get("response")).remove(missingField);
    NaverOAuth2UserInfo userInfo = new NaverOAuth2UserInfo(attributes);

    // when then
    assertThat(userInfo.validateNecessaryFields()).isFalse();
  }
}
