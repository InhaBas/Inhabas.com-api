package com.inhabas.api.domain.policy.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PolicyTermDtoTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  @BeforeAll
  public static void init() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  public static void close() {
    validatorFactory.close();
  }

  @DisplayName("title, content 가 null 이면 validation 실패")
  @Test
  void NotNull_Test() {
    // given
    PolicyTermDto policyTermDto =
        PolicyTermDto.builder().id(null).policyTypeId(null).title(null).content(null).build();

    // when
    Set<ConstraintViolation<PolicyTermDto>> violations = validator.validate(policyTermDto);

    // then
    assertThat(violations).hasSize(4);
  }
}
