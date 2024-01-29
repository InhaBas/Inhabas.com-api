package com.inhabas.api.auth.domain.oauth2.handler;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;

import com.inhabas.api.auth.AuthProperties;
import com.inhabas.api.auth.domain.error.authException.UnauthorizedRedirectUrlException;
import com.inhabas.api.auth.domain.oauth2.cookie.CookieUtils;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoFactory;
import com.inhabas.api.auth.domain.token.TokenUtil;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Slf4j
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final TokenUtil tokenUtil;
  private final AuthProperties authProperties;
  private final HttpCookieOAuth2AuthorizationRequestRepository
      httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    String targetUrl = this.determineTargetUrl(request, response, authentication);

    if (response.isCommitted()) {
      log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }

    this.clearAuthenticationAttributes(request);
    this.httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
    this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  /**
   * @param authentication 인증 완료된 결과
   * @return 인증 결과를 사용해서 access 토큰을 발급하고, 쿠키에 저장되어 있던 redirect_uri(프론트에서 적어준 것)와 합쳐서 반환. 명시되지 않으면
   *     설정파일({@link AuthProperties})에 명시된 default redirect url 값 적용
   */
  @Override
  protected String determineTargetUrl(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    String targetUrl =
        CookieUtils.resolveCookie(request, REDIRECT_URL_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse(authProperties.getOauth2().getDefaultRedirectUri());
    if (notAuthorized(targetUrl)) {
      /* 여기서 AuthenticationException 이 발생하면 예외는 AbstractAuthenticationProcessingFilter.doFilter 에서 처리된다.
       *   - AbstractAuthenticationProcessingFilter.doFilter 안에서 try~ catch~ 에서 잡힘.
       *   -    -> AbstractAuthenticationProcessingFilter.unsuccessfulAuthentication()
       *   -    -> Oauth2AuthenticationFailureHandler().onAuthenticationFailure()
       * */
      throw new UnauthorizedRedirectUrlException();
    }

    String imageUrl =
        OAuth2UserInfoFactory.getOAuth2UserInfo((OAuth2AuthenticationToken) authentication)
            .getImageUrl();

    return UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("accessToken", tokenUtil.createAccessToken(authentication))
        .queryParam("refreshToken", tokenUtil.createRefreshToken(authentication))
        .queryParam("expiresIn", tokenUtil.getExpiration())
        .queryParam("imageUrl", imageUrl)
        .build()
        .toUriString();
  }

  private boolean notAuthorized(String redirectUrl) {
    return !redirectUrl.isBlank()
        && !authProperties.getOauth2().isAuthorizedRedirectUri(redirectUrl);
  }
}
