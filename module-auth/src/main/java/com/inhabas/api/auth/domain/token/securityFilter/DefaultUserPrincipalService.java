package com.inhabas.api.auth.domain.token.securityFilter;

import org.springframework.security.core.Authentication;

public class DefaultUserPrincipalService implements UserPrincipalService {

  @Override
  public Object loadUserPrincipal(Authentication authentication) {
    return null;
  }
}
