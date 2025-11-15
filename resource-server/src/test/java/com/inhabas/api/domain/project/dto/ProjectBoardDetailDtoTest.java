package com.inhabas.api.domain.project.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import com.inhabas.api.domain.file.dto.FileDownloadDto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProjectBoardDetailDtoTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;
  private final List<FileDownloadDto> emptyList = new ArrayList<>();

  @BeforeAll
  public static void init() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  public static void close() {
    validatorFactory.close();
  }

  @DisplayName("ProjectBoardDetailDto 객체를 정상적으로 생성한다.")
  @Test
  public void ProjectBoardDetailDto_is_OK() {
    // given
    ProjectBoardDetailDto projectBoardDetailDto =
        new ProjectBoardDetailDto(
            1L,
            "title",
            "content",
            1L,
            "writer",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            emptyList,
            emptyList,
            false);

    // when
    Set<ConstraintViolation<ProjectBoardDetailDto>> violations =
        validator.validate(projectBoardDetailDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("projectBoardDetailDto title 필드가 null 이면 validation 실패")
  @Test
  public void Title_is_null() {
    // given
    ProjectBoardDetailDto projectBoardDetailDto =
        new ProjectBoardDetailDto(
            1L,
            null,
            "content",
            1L,
            "writer",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            emptyList,
            emptyList,
            false);

    // when
    Set<ConstraintViolation<ProjectBoardDetailDto>> violations =
        validator.validate(projectBoardDetailDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
