package com.inhabas.api.security.domain.token;

import io.jsonwebtoken.JwtException;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface TokenProvider {

    TokenDecodedInfo authenticate(String token) throws JwtException;

    String resolveToken(HttpServletRequest request) ;

    TokenDto reissueAccessTokenUsing(String refreshToken) throws JwtException;

    TokenDto createJwtToken(Integer memberId, Integer muId, String role, Set<String> teams);
}
