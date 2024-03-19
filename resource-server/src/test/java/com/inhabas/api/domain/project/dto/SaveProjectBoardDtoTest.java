package com.inhabas.api.domain.project.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.web.multipart.MultipartFile;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SaveProjectBoardDtoTest {

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

  @DisplayName("SaveProjectBoardDto 객체를 정상적으로 생성한다.")
  @Test
  public void SaveProjectBoardDto_is_OK() {
    // given
    SaveProjectBoardDto saveProjectBoardDto =
        new SaveProjectBoardDto("title", "content", emptyList, 2);

    // when
    Set<ConstraintViolation<SaveProjectBoardDto>> violations =
        validator.validate(saveProjectBoardDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("SaveProjectBoardDto content 필드가 null 이면 validation 실패")
  @Test
  public void Content_is_null() {
    // given
    SaveProjectBoardDto saveProjectBoardDto = new SaveProjectBoardDto("title", null, emptyList, 2);

    // when
    Set<ConstraintViolation<SaveProjectBoardDto>> violations =
        validator.validate(saveProjectBoardDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
