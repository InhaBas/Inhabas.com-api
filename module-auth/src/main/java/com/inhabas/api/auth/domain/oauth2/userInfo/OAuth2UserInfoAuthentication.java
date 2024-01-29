package com.inhabas.api.auth.domain.oauth2.userInfo;

import com.inhabas.api.auth.domain.token.TokenAuthenticationResult;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class OAuth2UserInfoAuthentication extends TokenAuthenticationResult {

  private final String uid;

  private final String provider;

  private final String email;

  public OAuth2UserInfoAuthentication(String uid, String provider, String email) {
    super(null);
    this.uid = uid;
    this.provider = provider;
    this.email = email;
  }

  protected OAuth2UserInfoAuthentication(
      String uid,
      String provider,
      String email,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.uid = uid;
    this.provider = provider;
    this.email = email;
  }

  public String getUid() {
    return uid;
  }

  public String getProvider() {
    return provider;
  }

  public String getEmail() {
    return email;
  }

  @Deprecated
  @Override
  public Object getCredentials() {
    return null;
  }
}
