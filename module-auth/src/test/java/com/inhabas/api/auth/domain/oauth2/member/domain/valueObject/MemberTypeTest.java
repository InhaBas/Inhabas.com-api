package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTypeTest {

  @DisplayName("MemberType 길이는 15자 초과하면 안된다.")
  @Test
  public void MemberTypeLengthMustNotBeGreaterThan15() {
    assertThat(
            Arrays.stream(MemberType.values())
                .filter(memberType -> memberType.toString().length() > 15)
                .findAny())
        .isEmpty();
  }

  @DisplayName("MemberType 값이 blank 값이면 안된다.")
  @Test
  public void MemberTypeValueMustNotBeBlank() {
    assertThat(
            Arrays.stream(MemberType.values())
                .filter(memberType -> memberType.toString().isBlank())
                .findAny())
        .isEmpty();
  }
}
