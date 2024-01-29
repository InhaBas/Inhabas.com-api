package com.inhabas.api.auth.domain.token;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.token.controller.JwtTokenController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ComponentScan(basePackages = "com.inhabas.api.auth.domain.token.controller")
@WebMvcTest(
    value = JwtTokenController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class JwtControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TokenReIssuer tokenReIssuer;

  @Autowired private ObjectMapper objectMapper;

  @DisplayName("accessToken 재발급")
  @Test
  public void reissueAccessTokenTest() throws Exception {
    // given
    TokenDto expectedNewTokenDto = new TokenDto("Bearer", "accessToken", "", 1L);
    given(tokenReIssuer.reissueAccessToken(any())).willReturn(expectedNewTokenDto);
    String requestJson = "{\"refreshToken\":\"helloworld\"}";

    // when
    mockMvc
        .perform(
            post("/token/refresh")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.grantType").value(equalTo("Bearer")))
        .andExpect(jsonPath("$.accessToken").value(equalTo("accessToken")));
  }
}
