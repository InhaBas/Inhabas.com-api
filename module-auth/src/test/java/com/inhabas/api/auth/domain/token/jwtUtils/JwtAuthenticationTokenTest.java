package com.inhabas.api.auth.domain.token.jwtUtils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtAuthenticationTokenTest {

  @DisplayName("토큰 문자열만으로 생성하면 인증되지 않은 상태이다.")
  @Test
  public void unauthenticatedTokenTest() {
    // given
    String jwt = "header.payload.signature";

    // when
    JwtAuthenticationToken authenticationToken = JwtAuthenticationToken.of(jwt);

    // then
    assertThat(authenticationToken.isAuthenticated()).isFalse();
    assertThat(authenticationToken.getPrincipal()).isEqualTo(jwt);
    assertThat(authenticationToken.getCredentials()).isEqualTo(jwt);
    assertThat(authenticationToken.getAuthorities()).isEmpty();
  }

  @DisplayName("principal, credentials, authorities 로 생성하면 인증된 상태이다.")
  @Test
  public void authenticatedTokenTest() {
    // given
    String jwt = "header.payload.signature";
    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_BASIC"));

    // when
    JwtAuthenticationToken authenticationToken = JwtAuthenticationToken.of(1L, jwt, authorities);

    // then
    assertThat(authenticationToken.isAuthenticated()).isTrue();
    assertThat(authenticationToken.getPrincipal()).isEqualTo(1L);
    assertThat(authenticationToken.getCredentials()).isEqualTo(jwt);
    assertThat(authenticationToken.getAuthorities())
        .containsExactly(new SimpleGrantedAuthority("ROLE_BASIC"));
  }
}
