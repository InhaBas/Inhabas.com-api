package com.inhabas.api.domain.normalBoard.dto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    SaveNormalBoardDto saveNormalBoardDto =
            new SaveNormalBoardDto("title", "content", emptyList, 2);

    // when
    Set<ConstraintViolation<SaveNormalBoardDto>> violations = validator.validate(saveNormalBoardDto);

    // then
    assertTrue(violations.isEmpty());
  }

  @DisplayName("SaveNormalBoardDto content 필드가 null 이면 validation 실패")
  @Test
  public void Content_is_null() {
    // given
    SaveNormalBoardDto saveNormalBoardDto = new SaveNormalBoardDto("title", null, emptyList,2);

    // when
    Set<ConstraintViolation<SaveNormalBoardDto>> violations = validator.validate(saveNormalBoardDto);

    // then
    assertEquals(1, violations.size());
  }

}
