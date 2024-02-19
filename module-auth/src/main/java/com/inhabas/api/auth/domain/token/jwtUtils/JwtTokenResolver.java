package com.inhabas.api.auth.domain.token.jwtUtils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.inhabas.api.auth.domain.token.TokenResolver;

public class JwtTokenResolver implements TokenResolver {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String AUTHORIZATION_TYPE = "Bearer ";

  @Override
  public String resolveAccessTokenOrNull(HttpServletRequest request) {

    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_TYPE))
      return bearerToken.substring(7);
    else return null;
  }
}
