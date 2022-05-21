package com.inhabas.api.auth.domain.oauth2.handler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;

public class Oauth2AuthenticationSuccessHandlerTest {

    private Oauth2AuthenticationSuccessHandler successHandler;

    // 리다이렉트 주소 어떻게 테스트?
    @Disabled
    @DisplayName("SuccessHandler 호출 시, targetURL 로 정상적으로 리다이렉트 된다.")
    @Test
    public void redirectToTargetUrlTest() {

    }

    // 리다이렉트 주소 어떻게 테스트?
    @Disabled
    @DisplayName("FailureHandler 호출 시, targetURL 로 정상적으로 리다이렉트 된다.")
    @Test
    public void unAuthorizedTargetUrlTest() throws IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(
                new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), Map.of("name", "ydh"), "name"),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), "google");

        Cookie redirectCookie = new Cookie(REDIRECT_URL_PARAM_COOKIE_NAME, "https://cross-site-request-forgery.com");
        request.setCookies(redirectCookie);

        //when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        //then
    }
}
