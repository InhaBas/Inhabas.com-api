package com.inhabas.api.auth.domain.oauth2.handler;

import com.inhabas.api.auth.domain.AuthProperties;
import com.inhabas.api.auth.domain.oauth2.BadRequestException;
import com.inhabas.api.auth.domain.oauth2.cookie.CookieUtils;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;

/**
 * default url 을 지정하면, 해당 url 로 리다이렉트.
 * 지정하지 않으면 401 응답.
 */
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final AuthProperties authProperties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        Optional<String> redirectUri = CookieUtils.resolveCookie(request, REDIRECT_URL_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // 허가된 redirect_uri 가 아님. (불법적인 시도)
        if (notAuthorized(redirectUri)) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri.orElse(authProperties.getOauth2().getDefaultRedirectUri()))
                .queryParam("error", getExceptionMessage(exception))
                .build().toUriString();

        httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private boolean notAuthorized(Optional<String> redirectUrl) {
        return redirectUrl.isPresent() &&
                !authProperties.getOauth2().isAuthorizedRedirectUri(redirectUrl.get());
    }


    private String getExceptionMessage(AuthenticationException exception) {

        if (exception instanceof OAuth2AuthenticationException) {
            return ((OAuth2AuthenticationException) exception).getError().getErrorCode();
        } else {
            return exception.getLocalizedMessage();
        }
    }

}
