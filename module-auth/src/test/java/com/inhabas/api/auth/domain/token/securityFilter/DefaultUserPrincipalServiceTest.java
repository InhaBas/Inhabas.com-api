package com.inhabas.api.auth.domain.token.securityFilter;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.security.authentication.TestingAuthenticationToken;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DefaultUserPrincipalServiceTest {

  @DisplayName("기본 UserPrincipalService 는 principal 로 null 을 반환한다.")
  @Test
  public void loadUserPrincipalTest() {
    // given
    DefaultUserPrincipalService defaultUserPrincipalService = new DefaultUserPrincipalService();

    // when
    Object principal =
        defaultUserPrincipalService.loadUserPrincipal(
            new TestingAuthenticationToken("user", "password"));

    // then
    assertThat(principal).isNull();
  }
}
