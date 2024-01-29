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

class SavePolicyTernDtoTest {

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
    SavePolicyTernDto savePolicyTernDto = SavePolicyTernDto.builder().content(null).build();

    // when
    Set<ConstraintViolation<SavePolicyTernDto>> violations = validator.validate(savePolicyTernDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
