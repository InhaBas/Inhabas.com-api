package com.inhabas.api.auth.domain.oauth2.member.security.masking;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MaskingTest {

  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }

  private void setAuthenticationWith(String role) {
    SecurityContextHolder.getContext()
        .setAuthentication(new TestingAuthenticationToken("user", "password", role));
  }

  @DisplayName("마스킹 타입이 null 이면 값을 그대로 반환한다.")
  @Test
  public void noMaskingTypeTest() {
    assertThat(Masking.mask(null, "010-1234-5678")).isEqualTo("010-1234-5678");
  }

  @DisplayName("이메일은 앞 3자리 이후의 로컬파트를 * 로 마스킹한다.")
  @Test
  public void emailMaskingTest() {
    assertThat(Masking.mask(MaskingType.EMAIL, "abcdefg@gmail.com")).isEqualTo("abc****@gmail.com");
  }

  @DisplayName("로컬파트가 3자리 이하인 이메일은 마스킹되지 않는다.")
  @Test
  public void shortEmailNotMaskedTest() {
    assertThat(Masking.mask(MaskingType.EMAIL, "abc@gmail.com")).isEqualTo("abc@gmail.com");
  }

  @DisplayName("일반 회원 권한은 전화번호가 010-****-**** 로 마스킹된다.")
  @ParameterizedTest
  @ValueSource(strings = {"010-1234-5678", "01012345678"})
  public void phoneMaskingForBasicRoleTest(String phone) {
    // given
    setAuthenticationWith("ROLE_BASIC");

    // when then
    assertThat(Masking.mask(MaskingType.PHONE, phone)).isEqualTo("010-****-****");
  }

  @DisplayName("총무/회장/부회장 권한은 전화번호가 마스킹되지 않는다.")
  @ParameterizedTest
  @ValueSource(strings = {"ROLE_SECRETARY", "ROLE_CHIEF", "ROLE_VICE_CHIEF"})
  public void phoneNotMaskedForPrivilegedRoleTest(String role) {
    // given
    setAuthenticationWith(role);

    // when then
    assertThat(Masking.mask(MaskingType.PHONE, "010-1234-5678")).isEqualTo("010-1234-5678");
  }

  @DisplayName("전화번호 형식이 아닌 값은 그대로 반환한다.")
  @Test
  public void invalidPhoneFormatNotMaskedTest() {
    // given
    setAuthenticationWith("ROLE_BASIC");

    // when then
    assertThat(Masking.mask(MaskingType.PHONE, "hello")).isEqualTo("hello");
  }
}
