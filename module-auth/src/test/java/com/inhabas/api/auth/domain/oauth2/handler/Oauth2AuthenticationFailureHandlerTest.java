package com.inhabas.api.auth.domain.oauth2.handler;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.IOException;

import javax.servlet.http.Cookie;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

import com.inhabas.api.auth.config.AuthProperties;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class Oauth2AuthenticationFailureHandlerTest {

  @InjectMocks private Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

  @Mock
  private HttpCookieOAuth2AuthorizationRequestRepository
      httpCookieOAuth2AuthorizationRequestRepository;

  @Mock private AuthProperties authProperties;

  @Mock private AuthProperties.OAuth2 oauth2Utils;


  private static final String VALID_REDIRECT_URL = "https://www.inhabas.com";
  private static final String INVALID_REDIRECT_URL = "https://www.unauthorized_url.com";
  private static final String ERROR_CODE = OAuth2ErrorCodes.INVALID_REQUEST;

  @BeforeEach
  public void setUp() {
    given(authProperties.getOauth2()).willReturn(oauth2Utils);
  }

  private MockHttpServletRequest createRequestWithCookie(String cookieValue) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    Cookie redirectCookie = new Cookie(REDIRECT_URL_PARAM_COOKIE_NAME, cookieValue);
    request.setCookies(redirectCookie);
    return request;
  }

  private AuthenticationException createAuthenticationException(String errorCode) {
    return new OAuth2AuthenticationException(errorCode);
  }


  @DisplayName("FailureHandler 호출 시, 허락된 defaultURL 로 정상적으로 리다이렉트 된다.")
  @Test
  public void redirectToDefaultTest() throws IOException {
    // given
    MockHttpServletRequest request = createRequestWithCookie(VALID_REDIRECT_URL);
    MockHttpServletResponse response = new MockHttpServletResponse();
    AuthenticationException authenticationException = new OAuth2AuthenticationException(ERROR_CODE);

    given(oauth2Utils.getDefaultRedirectUri()).willReturn(VALID_REDIRECT_URL);

    // when
    oauth2AuthenticationFailureHandler.onAuthenticationFailure(
        request, response, authenticationException);

    // then
    assertThat(response.getRedirectedUrl()).isEqualTo(VALID_REDIRECT_URL + "?error=" + ERROR_CODE);

  }

  @DisplayName("유효하지 않은 redirect_url 은 허용하지 않는다.")
  @Test
  public void validateRedirectUrlTest() throws IOException {
    // given
    MockHttpServletRequest request = createRequestWithCookie(INVALID_REDIRECT_URL);
    MockHttpServletResponse response = new MockHttpServletResponse();
    AuthenticationException authenticationException = createAuthenticationException(ERROR_CODE);

    given(oauth2Utils.getDefaultRedirectUri()).willReturn(VALID_REDIRECT_URL);

    // when
    oauth2AuthenticationFailureHandler.onAuthenticationFailure(
        request, response, authenticationException);

    // then
    assertThat(response.getRedirectedUrl()).isEqualTo(VALID_REDIRECT_URL + "?error=" + ERROR_CODE);
  }
}
