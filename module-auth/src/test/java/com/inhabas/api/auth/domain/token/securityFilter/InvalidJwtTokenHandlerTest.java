package com.inhabas.api.auth.domain.token.securityFilter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import jakarta.servlet.ServletException;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InvalidJwtTokenHandlerTest {

  @DisplayName("기본 실패 URL 이 없으면 리다이렉트 없이 401 을 반환하고 세션을 생성하지 않는다.")
  @Test
  public void respondWith401WithoutRedirectTest() throws IOException, ServletException {
    // given
    InvalidJwtTokenHandler invalidJwtTokenHandler = new InvalidJwtTokenHandler();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    // when
    invalidJwtTokenHandler.onAuthenticationFailure(request, response, new InvalidTokenException());

    // then
    assertThat(response.getStatus()).isEqualTo(401);
    assertThat(response.getRedirectedUrl()).isNull();
    assertThat(request.getSession(false)).isNull();
  }
}
