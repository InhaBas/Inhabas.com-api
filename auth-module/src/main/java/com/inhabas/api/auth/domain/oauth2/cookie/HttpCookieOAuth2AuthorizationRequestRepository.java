package com.inhabas.api.auth.domain.oauth2.cookie;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * IBAS 로그인 과정 종료 후에 프론트로 리다이렉션 시켜줘야하는데, 프론트에서 최초 요청 파라미터(redirect_uri)로 콜백 주소를 지정하게 해주기 위함.
 * OAuth2 Authorization Code Grant 과정에서 리다이렉션이 일어나므로 요청 파라미터를 브라우저 쿠키에 저장해야함.
 * 따라서 Oauth2AuthorizationRequest 를 쿠키에 저장.
 * @see org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
 */
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URL_PARAM_COOKIE_NAME = "redirect_url";
    private static final int cookieExpireSeconds = 180;


    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.resolveCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URL_PARAM_COOKIE_NAME);
            return;
        }
        CookieUtils.setCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUrlAfterLogin = request.getParameter(REDIRECT_URL_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUrlAfterLogin)) {
            CookieUtils.setCookie(response, REDIRECT_URL_PARAM_COOKIE_NAME, redirectUrlAfterLogin, cookieExpireSeconds);
        }
    }

    @Deprecated
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return null;
    }

    /**
     * OAuth2AuthorizationRequest 를 쿠키에서 제거함.
     * @return OAuth2AuthorizationRequest
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키 삭제하기 전에 쿠키 문자열을 객체로 변환
        OAuth2AuthorizationRequest authorizationRequest = this.loadAuthorizationRequest(request);
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);

        return authorizationRequest;
    }


    /**
     * redirect_url이 담긴 쿠키는 인증이 완전히 완료된 후에 제거되어야함.
     */
    public void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URL_PARAM_COOKIE_NAME);
    }
}
