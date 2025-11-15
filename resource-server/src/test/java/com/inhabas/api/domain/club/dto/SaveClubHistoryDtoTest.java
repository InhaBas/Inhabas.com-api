package com.inhabas.api.domain.club.dto;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SaveClubHistoryDtoTest {

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

  @DisplayName("dateHistory 가 null 이면 validation 실패")
  @Test
  void NotNull_Test() {
    // given
    SaveClubHistoryDto saveClubHistoryDto =
        SaveClubHistoryDto.builder()
            .title("goodTitle")
            .content("goodContent")
            .dateHistory(null)
            .build();

    // when
    Set<ConstraintViolation<SaveClubHistoryDto>> violations =
        validator.validate(saveClubHistoryDto);

    // then
    Assertions.assertThat(violations.size()).isEqualTo(1);
  }
}
