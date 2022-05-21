package com.inhabas.api.auth.domain.oauth2.handler;

import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

import javax.servlet.http.Cookie;
import java.io.IOException;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;

@ExtendWith(MockitoExtension.class)
public class Oauth2AuthenticationFailureHandlerTest {

    @InjectMocks
    private Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    // 리다이렉트 주소 어떻게 테스트?
    @Disabled
    @DisplayName("FailureHandler 호출 시, targetURL 로 정상적으로 리다이렉트 된다.")
    @Test
    public void redirectToTargetUrlTest() throws IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authenticationException = new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);

        Cookie redirectCookie = new Cookie(REDIRECT_URL_PARAM_COOKIE_NAME, "https://www.inhabas.com");
        request.setCookies(redirectCookie);

        //when
        oauth2AuthenticationFailureHandler.onAuthenticationFailure(request, response, authenticationException);

        //then
    }
}
