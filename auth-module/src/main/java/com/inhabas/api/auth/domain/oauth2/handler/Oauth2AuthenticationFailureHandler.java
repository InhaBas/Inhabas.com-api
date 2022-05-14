package com.inhabas.api.auth.domain.oauth2.handler;

import com.inhabas.api.auth.AuthProperties;
import com.inhabas.api.auth.domain.oauth2.cookie.CookieUtils;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.exception.UnauthorizedRedirectUrlException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_NAME;

@RequiredArgsConstructor
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final AuthProperties authProperties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private static final String ERROR_PARAM = "?error=";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        Optional<String> redirectUri = CookieUtils.resolveCookie(request, REDIRECT_URL_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = getAuthorizedTargetUrl(exception, redirectUri);

        httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    private String getAuthorizedTargetUrl(AuthenticationException exception, Optional<String> redirectUri) {

        StringBuilder targetUrl = new StringBuilder();
        if (exception instanceof UnauthorizedRedirectUrlException || redirectUri.isEmpty()) {
            targetUrl.append(authProperties.getOauth2().getDefaultRedirectUri());
        }
        else {
            targetUrl.append(redirectUri.get());
        }
        targetUrl.append(ERROR_PARAM).append(getExceptionMessage(exception));

        return targetUrl.toString();
    }


    private String getExceptionMessage(AuthenticationException exception) {

        if (exception instanceof OAuth2AuthenticationException) {
            return ((OAuth2AuthenticationException) exception).getError().getErrorCode();
        } else {
            return exception.getLocalizedMessage();
        }
    }

}
