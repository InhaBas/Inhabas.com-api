package com.inhabas.api.auth.domain.oauth2.handler;

import com.inhabas.api.auth.AuthProperties;
import com.inhabas.api.auth.domain.error.authException.UnauthorizedRedirectUrlException;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.domain.token.TokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class Oauth2AuthenticationSuccessHandlerTest {

    @InjectMocks
    private Oauth2AuthenticationSuccessHandler successHandler;

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository requestRepository;

    @Mock
    private AuthProperties authProperties;

    @Mock
    private AuthProperties.OAuth2 oAuth2Utils;

    private DefaultOAuth2User defaultOAuth2User;
    private final Set<SimpleGrantedAuthority> basicAuthorities =
            Collections.singleton(new SimpleGrantedAuthority("ROLE_BASIC"));


    @BeforeEach
    public void setUp() {
        given(authProperties.getOauth2()).willReturn(oAuth2Utils);
        defaultOAuth2User = new DefaultOAuth2User(
                basicAuthorities,
                Map.of("id", 1234, "properties", "blahblah"),
                "id"
        );
    }

    @DisplayName("SuccessHandler 호출 시, targetURL 로 정상적으로 리다이렉트 된다.")
    @Test
    public void redirectToTargetUrlTest() throws IOException {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthenticationToken authenticationToken =
                new OAuth2AuthenticationToken(defaultOAuth2User, basicAuthorities, "google");

        Cookie redirectCookie = new Cookie(REDIRECT_URL_PARAM_COOKIE_NAME,
                "https://www.inhabas.com");
        request.setCookies(redirectCookie);

        given(oAuth2Utils.isAuthorizedRedirectUri(any())).willReturn(true);

        //when
        successHandler.onAuthenticationSuccess(request, response, authenticationToken);

        //then
        assertThat(response.getRedirectedUrl())
                .contains("https://www.inhabas.com", "accessToken", "refreshToken", "expiresIn",
                        "imageUrl");
    }

    @DisplayName("인가되지 않은 redirect_url 요청 시, UnauthorizedRedirectUriException 발생")
    @Test
    public void unAuthorizedTargetUrlTest() {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthenticationToken authenticationToken =
                new OAuth2AuthenticationToken(defaultOAuth2User, basicAuthorities, "google");

        Cookie redirectCookie = new Cookie(REDIRECT_URL_PARAM_COOKIE_NAME,
                "https://www.inhabas.com");
        request.setCookies(redirectCookie);

        given(oAuth2Utils.isAuthorizedRedirectUri(any())).willReturn(false);

        //when
        Assertions.assertThrows(UnauthorizedRedirectUrlException.class,
                () -> successHandler.onAuthenticationSuccess(request, response,
                        authenticationToken));
    }

    @DisplayName("정상적으로 로그인 처리가 되고, 인증과정에서 생성된 쿠키는 삭제된다.")
    @Test
    public void clearCookieAfterHandleOAuth2Authentication() throws IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthenticationToken authenticationToken =
                new OAuth2AuthenticationToken(defaultOAuth2User, basicAuthorities, "google");

        Cookie redirectCookie = new Cookie(REDIRECT_URL_PARAM_COOKIE_NAME,
                "https://www.inhabas.com");
        request.setCookies(redirectCookie);

        given(oAuth2Utils.isAuthorizedRedirectUri(any())).willReturn(true);

        //when
        successHandler.onAuthenticationSuccess(request, response, authenticationToken);

        //when
        then(requestRepository).should(times(1)).clearCookies(any(), any());
    }
}
