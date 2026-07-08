package com.inhabas.api.auth.domain.oauth2.member.security.masking;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StringPropertyMaskerTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    objectMapper = new CustomObjectMapper().objectMapper();
  }

  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }

  private void setAuthenticationWith(String role) {
    SecurityContextHolder.getContext()
        .setAuthentication(new TestingAuthenticationToken("user", "password", role));
  }

  private static class MemberInfoDto {
    @Masked(type = MaskingType.PHONE)
    public String phone = "010-1234-5678";

    @Masked(type = MaskingType.EMAIL)
    public String email = "abcdefg@gmail.com";

    public String name = "유동현";

    public LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0);
  }

  @DisplayName("@Masked 필드만 마스킹되고, 어노테이션이 없는 필드는 그대로 직렬화된다.")
  @Test
  public void maskOnlyAnnotatedFieldsTest() throws JsonProcessingException {
    // given
    setAuthenticationWith("ROLE_BASIC");

    // when
    JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(new MemberInfoDto()));

    // then
    assertThat(json.get("phone").asText()).isEqualTo("010-****-****");
    assertThat(json.get("email").asText()).isEqualTo("abc****@gmail.com");
    assertThat(json.get("name").asText()).isEqualTo("유동현");
  }

  @DisplayName("총무 권한이면 전화번호가 마스킹되지 않는다.")
  @Test
  public void phoneNotMaskedForSecretaryTest() throws JsonProcessingException {
    // given
    setAuthenticationWith("ROLE_SECRETARY");

    // when
    JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(new MemberInfoDto()));

    // then
    assertThat(json.get("phone").asText()).isEqualTo("010-1234-5678");
  }

  @DisplayName("JavaTimeModule 이 등록되어 있어 LocalDateTime 필드도 직렬화된다.")
  @Test
  public void serializeLocalDateTimeTest() throws JsonProcessingException {
    // given
    setAuthenticationWith("ROLE_BASIC");

    // when
    JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(new MemberInfoDto()));

    // then
    assertThat(json.get("createdAt")).isNotNull();
  }
}
