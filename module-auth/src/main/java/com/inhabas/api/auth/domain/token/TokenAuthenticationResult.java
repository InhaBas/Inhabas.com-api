package com.inhabas.api.auth.domain.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public abstract class TokenAuthenticationResult extends AbstractAuthenticationToken {

  private Object principal;

  /**
   * Creates a token with the supplied array of authorities.
   *
   * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal represented
   *     by this authentication object.
   */
  public TokenAuthenticationResult(Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  public void setPrincipal(Object principal) {
    this.principal = principal;
  }
}
