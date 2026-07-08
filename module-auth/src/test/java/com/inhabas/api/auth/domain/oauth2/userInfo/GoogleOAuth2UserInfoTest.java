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

public class GoogleOAuth2UserInfoTest {

  private static Map<String, Object> googleAttributes() {
    return new HashMap<>(
        Map.of(
            "sub", "1249846925629348",
            "name", "유동현",
            "given_name", "동현",
            "family_name", "유",
            "picture", "https://lh3.googleusercontent.com/a/AATXAJzeE07A=s96-c",
            "email", "my@gmail.com",
            "email_verified", true,
            "locale", "ko"));
  }

  @DisplayName("구글 attributes 에서 id, name, email, imageUrl 을 읽어온다.")
  @Test
  public void getFieldsTest() {
    // given
    GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleAttributes());

    // then
    assertThat(userInfo.getId()).isEqualTo("1249846925629348");
    assertThat(userInfo.getName()).isEqualTo("유동현");
    assertThat(userInfo.getEmail()).isEqualTo("my@gmail.com");
    assertThat(userInfo.getImageUrl())
        .isEqualTo("https://lh3.googleusercontent.com/a/AATXAJzeE07A=s96-c");
    assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
  }

  @DisplayName("getAttributes 가 반환하는 map 은 수정할 수 없다.")
  @Test
  public void attributesAreUnmodifiableTest() {
    // given
    GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleAttributes());

    // when then
    assertThrows(
        UnsupportedOperationException.class, () -> userInfo.getAttributes().put("key", "value"));
  }

  @DisplayName("필수값이 모두 존재하면 validateNecessaryFields 는 true 를 반환한다.")
  @Test
  public void validateNecessaryFieldsTest() {
    // given
    GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleAttributes());

    // when then
    assertThat(userInfo.validateNecessaryFields()).isTrue();
  }

  @DisplayName("email, name, picture 중 하나라도 없으면 validateNecessaryFields 는 false 를 반환한다.")
  @ParameterizedTest
  @ValueSource(strings = {"email", "name", "picture"})
  public void validateNecessaryFieldsFailTest(String missingField) {
    // given
    Map<String, Object> attributes = googleAttributes();
    attributes.remove(missingField);
    GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(attributes);

    // when then
    assertThat(userInfo.validateNecessaryFields()).isFalse();
  }
}
