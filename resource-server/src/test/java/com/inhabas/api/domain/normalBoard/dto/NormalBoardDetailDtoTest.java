package com.inhabas.api.domain.normalBoard.dto;

import static org.assertj.core.api.Assertions.*;

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

class NormalBoardDetailDtoTest {

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

  @DisplayName("NormalBoardDetailDto 객체를 정상적으로 생성한다.")
  @Test
  public void NormalBoardDetailDto_is_OK() {
    // given
    NormalBoardDetailDto normalBoardDetailDto =
        new NormalBoardDetailDto(
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
    Set<ConstraintViolation<NormalBoardDetailDto>> violations =
        validator.validate(normalBoardDetailDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("normalBoardDetailDto title 필드가 null 이면 validation 실패")
  @Test
  public void Title_is_null() {
    // given
    NormalBoardDetailDto normalBoardDetailDto =
        new NormalBoardDetailDto(
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
    Set<ConstraintViolation<NormalBoardDetailDto>> violations =
        validator.validate(normalBoardDetailDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
