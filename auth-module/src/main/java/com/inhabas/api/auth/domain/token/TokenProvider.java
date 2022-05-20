package com.inhabas.api.auth.domain.token;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TokenProvider {

    TokenDecodedInfo authenticate(String token) throws JwtException;

    String resolveToken(HttpServletRequest request) ;

    TokenDto reissueAccessTokenUsing(String refreshToken) throws JwtException;

    /**
     * @param authentication the result of OAuth 2.0 authentication
     * @return jwt token string
     */
    String createAccessToken(Authentication authentication);

    /**
     * Some transactions may occur here whenever need to save refresh tokens.
     * @param authentication the result of OAuth 2.0 authentication
     * @return jwt token string
     */
    String createRefreshToken(Authentication authentication);

    /**
     * @return get {@code expires_in} response parameter value of the access token in seconds
     */
    Long getExpiration();
}
