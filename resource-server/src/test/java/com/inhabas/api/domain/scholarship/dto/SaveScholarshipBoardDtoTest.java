package com.inhabas.api.domain.scholarship.dto;

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

public class SaveScholarshipBoardDtoTest {
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

  @DisplayName("SaveScholarshipBoardDto 객체를 정상적으로 생성한다.")
  @Test
  public void SaveProjectBoardDto_is_OK() {
    // given
    SaveScholarshipBoardDto saveScholarshipBoardDto =
        new SaveScholarshipBoardDto("title", "content", LocalDateTime.now().minusDays(1L), null);

    // when
    Set<ConstraintViolation<SaveScholarshipBoardDto>> violations =
        validator.validate(saveScholarshipBoardDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("SaveScholarshipBoardDto title, content, dateHistory 필드가 null 이면 validation 실패")
  @Test
  public void Content_is_null() {
    // given
    SaveScholarshipBoardDto saveScholarshipBoardDto =
        new SaveScholarshipBoardDto(null, null, null, null);

    // when
    Set<ConstraintViolation<SaveScholarshipBoardDto>> violations =
        validator.validate(saveScholarshipBoardDto);

    // then
    assertThat(violations).hasSize(3);
  }
}
