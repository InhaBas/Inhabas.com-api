package com.inhabas.api.auth.domain.exception;

public interface AuthExceptionCodes {


    /**
     * {@code invalid_request} - The request is missing a required parameter, includes an
     * invalid parameter value, includes a parameter more than once, or is otherwise
     * malformed.
     */
    String INVALID_REQUEST = "invalid_request";

    /**
     * {@code access_denied} - The resource owner or authorization server denied the
     * request.
     */
    String ACCESS_DENIED = "access_denied";

    /**
     * {@code invalid_token} - The access token provided is expired, revoked, malformed,
     * or invalid for other reasons. The resource SHOULD respond with the HTTP 401
     * (Unauthorized) status code. The client MAY request a new access token and retry the
     * protected resource request.
     *
     * @see <a href="https://tools.ietf.org/html/rfc6750#section-3.1">RFC-6750 - Section
     * 3.1 - Error Codes</a>
     */
    String INVALID_TOKEN = "invalid_token";

    /**
     * {@code temporarily_unavailable} - The authorization server is currently unable to
     * handle the request due to a temporary overloading or maintenance of the server.
     * (This error code is needed because a 503 Service Unavailable HTTP status code
     * cannot be returned to the client via an HTTP redirect.)
     */
    String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";

    /**
     * {@code unsupported_token_type} - The authorization server does not support the
     * revocation of the presented token type.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7009#section-2.2.1">RFC-7009 - Section
     * 2.2.1 - Error Response</a>
     */
    String UNSUPPORTED_TOKEN_TYPE = "unsupported_token_type";

    /**
     * {@code unauthorized_redirect_uri} - The value of one or more redirection URIs is
     * unauthorized.
     */
    String UNAUTHORIZED_REDIRECT_URI = "unauthorized_redirect_uri";

    /**
     * {@code unsupported_oauth2_provider} - 지원하지 않는 소셜로그인
     */
    String UNSUPPORTED_OAUTH2_PROVIDER = "unsupported_oauth2_provider";


    /**
     * {@code invalid_user_info} - 인증에 필수적인 정보가 Oauth provider 로부터 전달되지 않았음.
     * 사용자가 개인정보 제공에 비동의했거나, 제대로 계정 정보를 설정하지 않은 경우 발생
     */
    String INVALID_USER_INFO = "invalid_user_info";

    /**
     * {@code user_not_found} - request 에 담긴 토큰정보를 사용해 기존 사용자 정보를 조회하였으나, 존재하지 않는 경우 발생.
     * 또는 최초 소셜로그인 시도하였으나 가입한 회원이 아니라면 해당 오류 발생
     * @see com.inhabas.api.auth.domain.token.securityFilter.TokenAuthenticationProcessingFilter
     */
    String USER_NOT_FOUND = "user_not_found";
}
