package com.inhabas.api.domain.scholarship.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SaveScholarshipHistoryDtoTest {
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

  @DisplayName("title, dateHistory 가 null 이면 validation 실패")
  @Test
  void NotNull_Test() {
    // given
    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        SaveScholarshipHistoryDto.builder().title(null).dateHistory(null).build();

    // when
    Set<ConstraintViolation<SaveScholarshipHistoryDto>> violations =
        validator.validate(saveScholarshipHistoryDto);

    // then
    assertThat(violations).hasSize(2);
  }
}
