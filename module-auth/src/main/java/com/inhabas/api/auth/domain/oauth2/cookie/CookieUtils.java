package com.inhabas.api.auth.domain.oauth2.cookie;

import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.SerializationUtils;

import io.micrometer.core.instrument.util.StringUtils;

public interface CookieUtils {

  enum SameSite {
    LAX("Lax"),
    STRICT("Strict"),
    NONE("None");
    private final String value;

    SameSite(String v) {
      this.value = v;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  /** request 에 담겨 있는 쿠키를 꺼낸다. */
  static Optional<Cookie> resolveCookie(HttpServletRequest request, String cookieName) {

    Cookie[] cookies = request.getCookies();

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(cookieName)) {
          return Optional.of(cookie);
        }
      }
    }

    return Optional.empty();
  }

  /** 기본 삭제: SameSite=Lax, Secure=request.isSecure(), path="/" 로 설정하여 Max-Age=0 으로 파기. */
  static void deleteCookie(
      HttpServletRequest request, HttpServletResponse response, String cookieName) {

    // 동일 이름 쿠키를 빈 값과 Max-Age=0 으로 덮어써서 삭제
    deleteCookie(request, response, cookieName, SameSite.LAX, null);
  }

  /**
   * 생성 시와 동일한 속성으로 삭제할 수 있도록 SameSite/Secure 를 제어하는 오버로드. secure=null 이면 request.isSecure() 사용.
   * SameSite=None 이면 Secure=true 강제.
   */
  static void deleteCookie(
      HttpServletRequest request,
      HttpServletResponse response,
      String cookieName,
      SameSite sameSite,
      Boolean secure) {

    boolean secureFlag = secure != null ? secure : request.isSecure();
    if (sameSite == SameSite.NONE && !secureFlag) {
      // 브라우저 정책: SameSite=None 은 Secure 필요. 강제 상승.
      secureFlag = true;
    }

    ResponseCookie rc =
        ResponseCookie.from(cookieName, "")
            .path("/")
            .httpOnly(true)
            .secure(secureFlag)
            .maxAge(Duration.ZERO)
            .sameSite(sameSite.toString())
            .build();
    response.addHeader("Set-Cookie", rc.toString());
  }

  /** 기본값: SameSite=Lax, Secure=request.isSecure() */
  static void setCookie(
      HttpServletRequest request,
      HttpServletResponse response,
      String cookieName,
      String cookieContents,
      int maxAge) {
    setCookie(request, response, cookieName, cookieContents, maxAge, SameSite.LAX, null);
  }

  /**
   * SameSite/Secure 를 제어할 수 있는 오버로드. secure=null 이면 request.isSecure() 사용. SameSite=None 이면
   * Secure=true 강제.
   */
  static void setCookie(
      HttpServletRequest request,
      HttpServletResponse response,
      String cookieName,
      String cookieContents,
      int maxAge,
      SameSite sameSite,
      Boolean secure) {

    boolean secureFlag = secure != null ? secure : request.isSecure();
    if (sameSite == SameSite.NONE && !secureFlag) {
      // 브라우저 정책: SameSite=None 은 Secure 필요. 강제 상승.
      secureFlag = true;
    }

    ResponseCookie rc =
        ResponseCookie.from(cookieName, cookieContents)
            .path("/")
            .httpOnly(true)
            .secure(secureFlag)
            .maxAge(Duration.ofSeconds(Math.max(0, maxAge)))
            .sameSite(sameSite.toString())
            .build();
    response.addHeader("Set-Cookie", rc.toString());
  }

  /**
   * @param request OAuth2AuthorizationRequest
   * @return 브라우저 쿠키에 담기 위해 OAuth2AuthorizationRequest 를 string 으로 변환.
   */
  static String serialize(OAuth2AuthorizationRequest request) {

    return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(request));
  }

  /**
   * @param cookie HttpServletRequest 로부터 resolve 한 쿠키
   * @param clz 반환 타입
   * @return string 으로 쿠키의 값을 clz 타입으로 반환
   */
  static <T> T deserialize(Cookie cookie, Class<T> clz) {

    if (isDeleted(cookie)) return null;
    else {
      try {
        return clz.cast(
            SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
      } catch (RuntimeException ex) { // Base64 decoding error or deserialization error
        return null;
      }
    }
  }

  private static boolean isDeleted(Cookie cookie) {
    return StringUtils.isBlank(cookie.getValue()) || Objects.isNull(cookie.getValue());
  }
}
