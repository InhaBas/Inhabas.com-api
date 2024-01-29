package com.inhabas.api.auth.domain.token.jwtUtils;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationResult extends OAuth2UserInfoAuthentication {

  private final Long memberId;

  public JwtAuthenticationResult(
      Long memberId,
      String uid,
      String provider,
      String email,
      Collection<? extends GrantedAuthority> authorities) {
    super(uid, provider, email, authorities);
    this.memberId = memberId;
  }
}
