package com.inhabas.api.auth.domain.oauth2.cookie;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import org.apache.commons.codec.binary.Base64;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CookieUtilsTest {

  private static final String COOKIE_NAME = "myCookie";
  private static final String COOKIE_CONTENTS = "hello";
  private static final int COOKIE_MAX_AGE = 180;

  @DisplayName("request 에서 쿠키를 꺼낸다.")
  @Test
  public void resolveCookieFromRequest() {
    // given
    MockHttpServletRequest request =
        createRequestWithCookie(COOKIE_NAME, COOKIE_CONTENTS, COOKIE_MAX_AGE);

    // when
    Optional<Cookie> myCookie = CookieUtils.resolveCookie(request, COOKIE_NAME);

    // then
    assertThat(myCookie)
        .isPresent()
        .hasValueSatisfying(
            cookie -> {
              assertThat(cookie.getValue()).isEqualTo(COOKIE_CONTENTS);
              assertThat(cookie.getMaxAge()).isEqualTo(COOKIE_MAX_AGE);
            });
  }

  @DisplayName("response 에 쿠키를 저장한다.")
  @Test
  public void saveCookieToResponse() {
    // given
    MockHttpServletResponse response = new MockHttpServletResponse();

    // when
    CookieUtils.setCookie(response, COOKIE_NAME, COOKIE_CONTENTS, COOKIE_MAX_AGE);

    // then
    Cookie resolvedCookie = response.getCookie(COOKIE_NAME);
    assertThat(resolvedCookie)
        .isNotNull()
        .satisfies(
            cookie -> {
              assertThat(cookie.getName()).isEqualTo(COOKIE_NAME);
              assertThat(cookie.getValue()).isEqualTo(COOKIE_CONTENTS);
              assertThat(cookie.getMaxAge()).isEqualTo(COOKIE_MAX_AGE);
            });
  }

  @DisplayName("request 에서 쿠키를 지운다.")
  @Test
  public void removeCookieOfRequest() {
    // given
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request =
        createRequestWithCookie(COOKIE_NAME, COOKIE_CONTENTS, COOKIE_MAX_AGE);

    // when
    CookieUtils.deleteCookie(request, response, COOKIE_NAME);

    // then
    Cookie deletedCookie = response.getCookie(COOKIE_NAME);
    assertThat(deletedCookie)
        .isNotNull()
        .satisfies(
            cookie -> {
              assertThat(cookie.getMaxAge()).isEqualTo(0);
              assertThat(cookie.getValue()).isEmpty();
            });
  }

  @DisplayName("성공적으로 serialize 한다.")
  @Test
  public void serializingTest()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
          IllegalAccessException {
    OAuth2AuthorizationRequest request = createOAuth2AuthorizationRequest();

    // when
    String serializedRequest = CookieUtils.serialize(request);

    // then
    assertThat(serializedRequest).matches(Base64::isBase64);
  }

  @DisplayName("성공적으로 deserialize 한다.")
  @Test
  public void deserializingTest()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
          IllegalAccessException {
    OAuth2AuthorizationRequest originalRequest = createOAuth2AuthorizationRequest();
    String serializedRequest = CookieUtils.serialize(originalRequest);
    Cookie cookie = new Cookie("base64", serializedRequest);

    // when
    OAuth2AuthorizationRequest deserializedRequest =
        CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);

    // then
    assertThat(deserializedRequest).usingRecursiveComparison().isEqualTo(originalRequest);
  }

  private MockHttpServletRequest createRequestWithCookie(String name, String value, int maxAge) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge(maxAge);
    request.setCookies(cookie);
    return request;
  }

  private static OAuth2AuthorizationRequest createOAuth2AuthorizationRequest()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
          IllegalAccessException {
    // reflection
    Constructor<?> constructor =
        OAuth2AuthorizationRequest.Builder.class.getDeclaredConstructor(
            AuthorizationGrantType.class);
    constructor.setAccessible(true);

    OAuth2AuthorizationRequest.Builder builder =
        (OAuth2AuthorizationRequest.Builder)
            constructor.newInstance(AuthorizationGrantType.AUTHORIZATION_CODE);
    return builder
        .authorizationUri("https://kauth.kakao.com/oauth/authorize")
        .clientId("1234")
        .redirectUri("http://localhost/api/login/oauth2/code/kakao")
        .scopes(Set.of("gender", "profile_image", "account_email", "profile_nickname"))
        .state("state1934")
        .additionalParameters(java.util.Map.of())
        .attributes(java.util.Map.of("registration_id", "kakao"))
        .build();
  }
}
