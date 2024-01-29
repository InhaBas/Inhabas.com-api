package com.inhabas.api.auth.domain.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class CustomOAuth2User extends DefaultOAuth2User {

  private Long memberId;

  public CustomOAuth2User(
      Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes,
      String nameAttributeKey,
      Long memberId) {
    super(authorities, attributes, nameAttributeKey);
    this.memberId = memberId;
  }

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }
}
