package com.inhabas.api.auth.domain.oauth2.cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

  @DisplayName("request 에서 쿠키를 꺼낸다.")
  @Test
  public void resolveCookieFromRequest() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    Cookie cookie = new Cookie("myCookie", "hello");
    cookie.setMaxAge(180);
    request.setCookies(cookie);

    // when
    Optional<Cookie> myCookie = CookieUtils.resolveCookie(request, "myCookie");

    // then
    assertThat(myCookie.isPresent()).isTrue();
    assertThat(myCookie.get().getValue()).isEqualTo("hello");
    assertThat(myCookie.get().getMaxAge()).isEqualTo(180);
  }

  @DisplayName("response 에 쿠키를 저장한다.")
  @Test
  public void saveCookieToResponse() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    String cookieName = "myCookie";
    String cookieContents = "hello";

    // when
    CookieUtils.setCookie(request, response, cookieName, cookieContents, 180);

    // then
    Cookie resolvedCookie = response.getCookie(cookieName);
    assert resolvedCookie != null;

    assertThat(resolvedCookie.getName()).isEqualTo(cookieName);
    assertThat(resolvedCookie.getValue()).isEqualTo(cookieContents);
    assertThat(resolvedCookie.getMaxAge()).isEqualTo(180);
  }

  @DisplayName("request 에서 쿠키를 지운다.")
  @Test
  public void removeCookieOfRequest() {
    // given
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest();
    Cookie cookie = new Cookie("myCookie", "hello");
    cookie.setMaxAge(180);
    request.setCookies(cookie);

    // when
    CookieUtils.deleteCookie(request, response, "myCookie");

    // then
    Cookie deletedCookie = response.getCookie("myCookie");
    assert deletedCookie != null;
    assertThat(deletedCookie.getMaxAge()).isEqualTo(0);
    assertThat(deletedCookie.getValue()).isEqualTo("");
  }

  @DisplayName("성공적으로 serialize 한다.")
  @Test
  public void serializingTest()
      throws InvocationTargetException, InstantiationException, IllegalAccessException,
          NoSuchMethodException {
    // reflection
    Constructor<?> constructor =
        OAuth2AuthorizationRequest.Builder.class.getDeclaredConstructor(
            AuthorizationGrantType.class);
    constructor.setAccessible(true);

    // given
    OAuth2AuthorizationRequest.Builder builder =
        (OAuth2AuthorizationRequest.Builder)
            constructor.newInstance(AuthorizationGrantType.AUTHORIZATION_CODE);
    OAuth2AuthorizationRequest requestObj =
        builder
            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
            .clientId("1234")
            .redirectUri("http://localhost/api/login/oauth2/code/kakao")
            .scopes(Set.of("gender", "profile_image", "account_email", "profile_nickname"))
            .state("state1934")
            .additionalParameters(java.util.Map.of())
            .attributes(java.util.Map.of("registration_id", "kakao"))
            .build();

    // when
    String serializedRequest = CookieUtils.serialize(requestObj);

    // then
    assertTrue(Base64.isBase64(serializedRequest));
  }

  @DisplayName("성공적으로 deserialize 한다.")
  @Test
  public void deserializingTest()
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
          IllegalAccessException {

    // reflection
    Constructor<?> constructor =
        OAuth2AuthorizationRequest.Builder.class.getDeclaredConstructor(
            AuthorizationGrantType.class);
    constructor.setAccessible(true);

    // given
    OAuth2AuthorizationRequest.Builder builder =
        (OAuth2AuthorizationRequest.Builder)
            constructor.newInstance(AuthorizationGrantType.AUTHORIZATION_CODE);
    OAuth2AuthorizationRequest originalRequest =
        builder
            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
            .clientId("1234")
            .redirectUri("http://localhost/api/login/oauth2/code/kakao")
            .scopes(Set.of("gender", "profile_image", "account_email", "profile_nickname"))
            .state("state1934")
            .additionalParameters(java.util.Map.of())
            .attributes(java.util.Map.of("registration_id", "kakao"))
            .build();

    String serializedRequest = CookieUtils.serialize(originalRequest);
    Cookie cookie = new Cookie("base64", serializedRequest);

    // when
    OAuth2AuthorizationRequest deserializedRequest =
        CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);

    // then
    assertThat(deserializedRequest).usingRecursiveComparison().isEqualTo(originalRequest);
  }

  @DisplayName("역직렬화 실패 시 null 반환한다.")
  @Test
  public void deserializeReturnsNullOnInvalidBase64OrPayload() {
    // given
    Cookie invalid = new Cookie("bad", "not_base64!!");

    // when
    OAuth2AuthorizationRequest result =
        CookieUtils.deserialize(invalid, OAuth2AuthorizationRequest.class);

    // then
    assertThat(result).isNull();
  }

  @DisplayName("setCookie 시 SameSite=Lax 헤더를 추가한다.")
  @Test
  public void setCookieAddsSameSiteHeader() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    // when
    CookieUtils.setCookie(request, response, "s", "v", 60);

    // then
    java.util.List<String> setCookies = response.getHeaders("Set-Cookie");
    assertThat(setCookies).isNotEmpty();
    assertThat(String.join(" ", setCookies)).contains("SameSite=Lax");
  }

  @DisplayName("deleteCookie 시 SameSite=Lax, Max-Age=0, HttpOnly를 포함한다.")
  @Test
  public void deleteCookieAddsAttributesProperly() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    Cookie cookie = new Cookie("del", "bye");
    cookie.setMaxAge(120);
    request.setCookies(cookie);

    // when
    CookieUtils.deleteCookie(request, response, "del");

    // then
    java.util.List<String> setCookies = response.getHeaders("Set-Cookie");
    assertThat(setCookies).isNotEmpty();
    String combined = String.join(" ", setCookies);
    assertThat(combined).contains("SameSite=Lax");
    assertThat(combined).contains("Max-Age=0");
    assertThat(combined).contains("HttpOnly");
  }
}
