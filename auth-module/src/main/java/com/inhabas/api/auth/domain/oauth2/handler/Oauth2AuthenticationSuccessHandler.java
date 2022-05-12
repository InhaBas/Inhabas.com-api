package com.inhabas.api.auth.domain.oauth2.handler;

import com.inhabas.api.auth.domain.AuthProperties;
import com.inhabas.api.auth.domain.oauth2.BadRequestException;
import com.inhabas.api.auth.domain.oauth2.cookie.CookieUtils;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.domain.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final AuthProperties authProperties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = this.determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        this.clearAuthenticationAttributes(request);
        this.httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
        this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * @param authentication 인증 완료된 결과
     * @return 인증 결과를 사용해서 access 토큰을 발급하고, 쿠키에 저장되어 있던 redirect_uri(프론트에서 적어준 것)와 합쳐서 반환. 명시되지 않으면 default 값 적용
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.resolveCookie(request, REDIRECT_URL_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // 허가된 redirect_uri 가 아님. (불법적인 시도)
        if (notAuthorized(redirectUri)) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(authProperties.getOauth2().getDefaultRedirectUri());
//        String token = tokenProvider.createToken(authentication);
        return UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("token", token)
                .build().toUriString();
    }

    private boolean notAuthorized(Optional<String> redirectUrl) {
        return redirectUrl.isPresent() &&
                !authProperties.getOauth2().isAuthorizedRedirectUri(redirectUrl.get());
    }
}
