package com.inhabas.api.auth.domain.oauth2.cookie;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Component
public interface CookieUtils {

    /**
     * request 에 담겨 있는 쿠키를 꺼낸다.
     */
    static Optional<Cookie> resolveCookie(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }


    /**
     * 쿠키를 지우는 작업은 없고, maxAge 를 0으로 설정해서 브라우저가 파기하도록 한다.
     */
    static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    /**
     * @param response 응답에 쿠키를 적어서 보내줌
     * @param cookieName key
     * @param cookieContents value
     * @param maxAge 초 단위
     */
    static void setCookie(HttpServletResponse response, String cookieName, String cookieContents, int maxAge) {

        Cookie cookie = new Cookie(cookieName, cookieContents);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }


    /**
     * @param request OAuth2AuthorizationRequest
     * @return 브라우저 쿠키에 담기 위해 OAuth2AuthorizationRequest 를 string 으로 변환.
     */
    static String serialize(OAuth2AuthorizationRequest request) {

        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(request));
    }


    /**
     * @param cookie HttpServletRequest 로부터 resolve 한 쿠키
     * @param clz 반환 타입
     * @return string 으로 쿠키의 값을 clz 타입으로 반환
     */
    static <T> T deserialize(Cookie cookie, Class<T> clz) {

        if (isDeleted(cookie))
            return null;
        else
            return clz.cast(SerializationUtils.deserialize(
                    Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    private static boolean isDeleted(Cookie cookie) {
        return StringUtils.isBlank(cookie.getValue()) || Objects.isNull(cookie.getValue());
    }
}
