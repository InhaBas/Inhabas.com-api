package com.inhabas.api.domain.normalBoard.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.springframework.web.multipart.MultipartFile;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SaveNormalBoardDtoTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;
  private final List<MultipartFile> emptyList = new ArrayList<>();

  @BeforeAll
  public static void init() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  public static void close() {
    validatorFactory.close();
  }

  @DisplayName("SaveNormalBoardDto 객체를 정상적으로 생성한다.")
  @Test
  public void SaveNormalBoardDto_is_OK() {
    // given
    SaveNormalBoardDto saveNormalBoardDto = new SaveNormalBoardDto("title", "content", null, 2);

    // when
    Set<ConstraintViolation<SaveNormalBoardDto>> violations =
        validator.validate(saveNormalBoardDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("SaveNormalBoardDto content 필드가 null 이면 validation 실패")
  @Test
  public void Content_is_null() {
    // given
    SaveNormalBoardDto saveNormalBoardDto = new SaveNormalBoardDto("title", null, null, 2);

    // when
    Set<ConstraintViolation<SaveNormalBoardDto>> violations =
        validator.validate(saveNormalBoardDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
