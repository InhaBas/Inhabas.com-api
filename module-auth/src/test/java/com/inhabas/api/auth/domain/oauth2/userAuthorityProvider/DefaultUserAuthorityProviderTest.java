package com.inhabas.api.auth.domain.oauth2.userAuthorityProvider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.inhabas.api.auth.domain.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DefaultUserAuthorityProviderTest {

  @DisplayName("기본 권한 제공자는 항상 ROLE_ANONYMOUS 하나만 반환한다.")
  @Test
  public void determineAuthoritiesTest() {
    // given
    DefaultUserAuthorityProvider defaultUserAuthorityProvider = new DefaultUserAuthorityProvider();
    OAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(Map.of("sub", "1234567891011121314"));

    // when
    Collection<SimpleGrantedAuthority> authorities =
        defaultUserAuthorityProvider.determineAuthorities(userInfo);

    // then
    assertThat(authorities).containsExactly(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
  }
}
