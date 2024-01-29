package com.inhabas.api.domain.policy.domain;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PolicyTermTest {

  @DisplayName("Content는 null 일 수 없다.")
  @Test
  void Content_Notnull() {
    // given
    PolicyType policyType = new PolicyType("goodTitle");
    String nullString = null;

    // then
    Assertions.assertThatThrownBy(() -> new PolicyTerm(policyType, nullString))
        .isInstanceOf(InvalidInputException.class)
        .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
  }

  @DisplayName("올바른 PolicyTerm을 생성한다.")
  @Test
  void createPolicyTerm() {
    // given
    PolicyType policyType = new PolicyType("goodTitle");
    String goodContent = "goodContent";

    // when
    PolicyTerm policyTerm = new PolicyTerm(policyType, goodContent);

    // then
    Assertions.assertThat(policyTerm.getContent().getValue()).isEqualTo(goodContent);
  }
}
