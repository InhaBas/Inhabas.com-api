package com.inhabas.api.auth.domain.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.mock.web.MockHttpServletRequest;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenResolver;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtTokenResolverTest {

  private static final String VALID_AUTHORIZATION_HEADER = "Bearer header.body.signature";
  private static final String VALID_AUTHORIZATION_HEADER_RESULT = "header.body.signature";
  private static final String INVALID_AUTHORIZATION_HEADER = "No-Bearer header.body.signature";
  private static final String AUTHORIZATION = "Authorization";
  private final JwtTokenResolver jwtTokenResolver = new JwtTokenResolver();

  @DisplayName("http request Authorization header 로부터 토큰을 꺼낸다.")
  @Test
  public void resolveTokenFromHttpRequestTest() {

    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(AUTHORIZATION, VALID_AUTHORIZATION_HEADER);

    // when
    String resolvedToken = jwtTokenResolver.resolveAccessTokenOrNull(request);

    // then
    assertThat(resolvedToken).isEqualTo(VALID_AUTHORIZATION_HEADER_RESULT);
  }

  @DisplayName("http request 에 Authorization header 가 설정되어 있지 않아서 null을 반환한다.")
  @Test
  public void cannotResolveTokenFromHttpRequestTest() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();

    // when
    String resolvedToken = jwtTokenResolver.resolveAccessTokenOrNull(request);

    // then
    Assertions.assertThat(resolvedToken).isNull();
  }

  @DisplayName("Bearer 토큰이 아니면 null 을 반환한다.")
  @Test
  public void cannotResolveInvalidTokenFromHttpRequestTest() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "No-Bearer header.body.signature");

    // when
    String resolvedToken = jwtTokenResolver.resolveAccessTokenOrNull(request);

    // then
    Assertions.assertThat(resolvedToken).isNull();
  }
}
