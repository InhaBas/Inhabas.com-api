package com.inhabas.api.domain.normalBoard.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NormalBoardDtoTest {

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

  @DisplayName("NormalBoardDto 객체를 정상적으로 생성한다.")
  @Test
  public void NormalBoardDto_is_OK() {
    // given
    NormalBoardDto normalBoardDto =
        new NormalBoardDto(
            1L,
            "title",
            1L,
            "writer",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            false);

    // when
    Set<ConstraintViolation<NormalBoardDto>> violations = validator.validate(normalBoardDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("NormalBoardDto title 필드가 null 이면 validation 실패")
  @Test
  public void Title_is_null() {
    // given
    NormalBoardDto normalBoardDto =
        new NormalBoardDto(
            1L,
            null,
            1L,
            "writer",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            false);

    // when
    Set<ConstraintViolation<NormalBoardDto>> violations = validator.validate(normalBoardDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
