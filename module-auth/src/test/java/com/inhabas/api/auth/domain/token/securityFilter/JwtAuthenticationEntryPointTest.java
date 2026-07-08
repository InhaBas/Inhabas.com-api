package com.inhabas.api.auth.domain.token.securityFilter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtAuthenticationEntryPointTest {

  @DisplayName("인증 실패 시 403 상태와 AUTHORITY_INVALID 에러 응답을 내려준다.")
  @Test
  public void commenceTest() throws IOException, ServletException {
    // given
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    // when
    jwtAuthenticationEntryPoint.commence(
        request, response, new InsufficientAuthenticationException("인증 정보가 없습니다."));

    // then
    assertThat(response.getStatus()).isEqualTo(403);
    assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

    JsonNode body =
        new ObjectMapper().readTree(response.getContentAsString(StandardCharsets.UTF_8));
    assertThat(body.get("status").asInt()).isEqualTo(403);
    assertThat(body.get("code").asText()).isEqualTo("M002");
    assertThat(body.get("message").asText()).isEqualTo("권한이 없습니다.");
  }
}
