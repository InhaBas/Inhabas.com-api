package com.inhabas.api.domain.policy.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PolicyTypeTest {

  @DisplayName("Title은 null, empty일 수 없다.")
  @Test
  void Title_Notnull_NotEmpty() {
    // given
    String white = "\t";

    // then
    assertThatThrownBy(() -> new PolicyType(white))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("올바른 PolicyType을 생성한다.")
  @Test
  void createPolicyType() {
    // given
    String goodTitle = "good";

    // when
    PolicyType policyType = new PolicyType(goodTitle);

    // then
    Assertions.assertThat(policyType.getTitle()).isEqualTo(goodTitle);
  }
}
