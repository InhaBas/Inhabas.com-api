package com.inhabas.api.auth.domain.oauth2;

import java.util.Collection;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Setter
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

  private Long memberId;
  private String memberName;
  private String memberPicture;

  public CustomOAuth2User(
      Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes,
      String nameAttributeKey,
      Long memberId,
      String memberName,
      String memberPicture) {
    super(authorities, attributes, nameAttributeKey);
    this.memberId = memberId;
    this.memberName = memberName;
    this.memberPicture = memberPicture;
  }
}
