package com.inhabas.api.auth.domain.token.jwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final JwtTokenUtil jwtTokenUtil;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String jwt = (String) authentication.getPrincipal();
    return jwtTokenUtil.getAuthentication(jwt);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
