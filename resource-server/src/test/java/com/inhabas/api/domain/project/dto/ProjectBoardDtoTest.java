package com.inhabas.api.domain.project.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProjectBoardDtoTest {

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

  @DisplayName("ProjectBoardDto 객체를 정상적으로 생성한다.")
  @Test
  public void ProjectBoardDto_is_OK() {
    // given
    ProjectBoardDto projectBoardDto =
        new ProjectBoardDto(
            1L,
            "title",
            1L,
            "writer",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            false);

    // when
    Set<ConstraintViolation<ProjectBoardDto>> violations = validator.validate(projectBoardDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("ProjectBoardDto title 필드가 null 이면 validation 실패")
  @Test
  public void Title_is_null() {
    // given
    ProjectBoardDto projectBoardDto =
        new ProjectBoardDto(
            1L,
            null,
            1L,
            "writer",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            false);

    // when
    Set<ConstraintViolation<ProjectBoardDto>> violations = validator.validate(projectBoardDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
