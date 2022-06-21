package com.inhabas.api.auth.domain.oauth2.cookie;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.Cookie;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.Predicate;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpCookieOAuth2AuthorizationRequestRepositoryTest {


    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository =
            new HttpCookieOAuth2AuthorizationRequestRepository();


    @DisplayName("Request 쿠키에서 OAuth2AuthorizationRequest 객체를 복원한다.")
    @Test
    public void loadAuthorizationRequestTest() throws NoSuchMethodException {
        //given
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = createOAuth2AuthorizationRequest();
        Cookie cookie = new Cookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(oAuth2AuthorizationRequest));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(180);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(cookie);

        //when
        OAuth2AuthorizationRequest restoredOAuth2AuthorizationRequest =
                httpCookieOAuth2AuthorizationRequestRepository.loadAuthorizationRequest(request);

        //then
        assertThat(restoredOAuth2AuthorizationRequest)
                .usingRecursiveComparison()
                .isEqualTo(oAuth2AuthorizationRequest);
    }


    @DisplayName("OAuth2AuthorizationRequest 가 null 이면 관련 쿠키는 아무것도 없다.")
    @Test
    public void saveAuthorizationRequestNullTest() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        httpCookieOAuth2AuthorizationRequestRepository.saveAuthorizationRequest(null, request, response);

        //then
        assertThat(response.getCookies())
                .filteredOn(cookie ->
                        cookie.getName().equals(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME) ||
                        cookie.getName().equals(REDIRECT_URL_PARAM_COOKIE_NAME))
                .allMatch(cookieIsNull());
    }


    @DisplayName("OAuth2AuthorizationRequest 를 성공적으로 쿠키로 저장한다.")
    @Test
    public void saveAuthorizationRequestTest() throws NoSuchMethodException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = this.createOAuth2AuthorizationRequest();

        //when
        httpCookieOAuth2AuthorizationRequestRepository.saveAuthorizationRequest(oAuth2AuthorizationRequest, request, response);

        //then
        // 쿠키 한가지 존재하는지 확인.
        Cookie savedCookie = response.getCookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        assert savedCookie != null;
        assertTrue(Base64.isBase64(savedCookie.getValue()));
    }


    @DisplayName("OAuth2AuthorizationRequest 를 쿠키로 저장할 때, redirect_url 도 쿠키로 저장한다.")
    @Test
    public void saveAuthorizationRequestWithRedirectUrlTest() throws NoSuchMethodException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = this.createOAuth2AuthorizationRequest();

        request.setParameter(REDIRECT_URL_PARAM_COOKIE_NAME, "/index.html");

        //when
        httpCookieOAuth2AuthorizationRequestRepository.saveAuthorizationRequest(oAuth2AuthorizationRequest, request, response);

        //then
        // 쿠키 두가지 존재하는 지 확인
        assertThat(response.getCookies())
                .filteredOn(cookie ->
                        cookie.getName().equals(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME) ||
                        cookie.getName().equals(REDIRECT_URL_PARAM_COOKIE_NAME))
                .allMatch(cookie -> !cookie.getValue().isBlank());
    }


    @DisplayName("OAuth2AuthorizationRequest 를 성공적으로 쿠키에서 삭제한다. (redirectUrl 쿠키는 삭제되지 않는다.)")
    @Test
    public void removeAuthorizationRequestTest() throws NoSuchMethodException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = this.createOAuth2AuthorizationRequest();

        Cookie serializedAuthorizationRequest = new Cookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(oAuth2AuthorizationRequest));
        request.setCookies(serializedAuthorizationRequest);

        //when
        OAuth2AuthorizationRequest returnedRequest =
                httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(request, response);

        //then
        Cookie cookie = response.getCookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        assert cookie != null;
        assertTrue(cookie.getValue().isBlank() && cookie.getMaxAge() == 0);

    }


    @DisplayName("OAuth2AuthorizationRequest 를 성공적으로 쿠키에서 삭제한다. (redirectUrl 쿠키도 삭제된다.)")
    @Test
    public void removeAuthorizationRequestTotally() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);

        //then
        assertThat(response.getCookies())
                .filteredOn(cookie ->
                        cookie.getName().equals(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME) ||
                        cookie.getName().equals(REDIRECT_URL_PARAM_COOKIE_NAME))
                .allMatch(cookieIsNull());
    }





    private Predicate<Cookie> cookieIsNull() {
        return cookie ->
                cookie.getValue().isBlank() && cookie.getMaxAge() == 0;
    }


    private OAuth2AuthorizationRequest createOAuth2AuthorizationRequest() throws NoSuchMethodException {
        //reflection
        Constructor<?> constructor =
                OAuth2AuthorizationRequest.Builder.class.getDeclaredConstructor(AuthorizationGrantType.class);
        constructor.setAccessible(true);

        //construct
        try {
            OAuth2AuthorizationRequest.Builder builder =
                    (OAuth2AuthorizationRequest.Builder) constructor.newInstance(AuthorizationGrantType.AUTHORIZATION_CODE);

            return builder.authorizationUri("https://kauth.kakao.com/oauth/authorize")
                    .clientId("1234")
                    .redirectUri("http://localhost/api/login/oauth2/code/kakao")
                    .scopes(Set.of("gender", "profile_image", "account_email", "profile_nickname"))
                    .state("state1934")
                    .additionalParameters(java.util.Map.of())
                    .attributes(java.util.Map.of("registration_id", "kakao")).build();

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ignored) {
            return null;
        }
    }

}
