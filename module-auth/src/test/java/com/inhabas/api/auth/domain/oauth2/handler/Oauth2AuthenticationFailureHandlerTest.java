package com.inhabas.api.auth.domain.oauth2.handler;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.inhabas.api.auth.AuthProperties;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import java.io.IOException;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
public class Oauth2AuthenticationFailureHandlerTest {

  @InjectMocks private Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

  @Mock
  private HttpCookieOAuth2AuthorizationRequestRepository
      httpCookieOAuth2AuthorizationRequestRepository;

  @Mock private AuthProperties authProperties;

  @Mock private AuthProperties.OAuth2 oauth2Utils;

  @BeforeEach
  public void setUp() {
    given(authProperties.getOauth2()).willReturn(oauth2Utils);
  }

  @DisplayName("FailureHandler 호출 시, 허락된 defaultURL 로 정상적으로 리다이렉트 된다.")
  @Test
  public void redirectToDefaultTest() throws IOException {
    // given
    String errorCode = OAuth2ErrorCodes.INVALID_REQUEST;
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    AuthenticationException authenticationException = new OAuth2AuthenticationException(errorCode);

    Cookie redirectCookie = new Cookie(REDIRECT_URL_PARAM_COOKIE_NAME, "https://www.inhabas.com");
    request.setCookies(redirectCookie);

    given(oauth2Utils.getDefaultRedirectUri()).willReturn("https://www.inhabas.com");

    // when
    oauth2AuthenticationFailureHandler.onAuthenticationFailure(
        request, response, authenticationException);

    // then
    assertThat(response.getRedirectedUrl()).isEqualTo("https://www.inhabas.com?error=" + errorCode);
  }

  @DisplayName("유효하지 않은 redirect_url 은 허용하지 않는다.")
  @Test
  public void validateRedirectUrlTest() throws IOException {
    // given
    String errorCode = OAuth2ErrorCodes.INVALID_REQUEST;
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    AuthenticationException authenticationException = new OAuth2AuthenticationException(errorCode);

    Cookie redirectCookie =
        new Cookie(REDIRECT_URL_PARAM_COOKIE_NAME, "https://www.unauthorized_url.com");
    request.setCookies(redirectCookie);

    given(oauth2Utils.getDefaultRedirectUri()).willReturn("https://www.inhabas.com");

    // when
    oauth2AuthenticationFailureHandler.onAuthenticationFailure(
        request, response, authenticationException);

    // then
    assertThat(response.getRedirectedUrl()).isEqualTo("https://www.inhabas.com?error=" + errorCode);
  }
}
