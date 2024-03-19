package com.inhabas.api.domain.myInfo.dto;

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

public class MyBoardsDtoTest {

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

  @DisplayName("MyBoardsDto 객체를 정상적으로 생성한다.")
  @Test
  public void MyBoardsDto_is_OK() {
    // given
    MyBoardsDto myBoardsDto =
        MyBoardsDto.builder()
            .id(1L)
            .menuId(16)
            .menuName("알파 테스터")
            .title("title")
            .dateCreated(LocalDateTime.now())
            .build();

    // when
    Set<ConstraintViolation<MyBoardsDto>> violations = validator.validate(myBoardsDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("MyBoardsDto menuName 필드가 null 이면 validation 실패")
  @Test
  public void MenuName_is_null() {
    // given
    MyBoardsDto myBoardsDto =
        MyBoardsDto.builder()
            .id(1L)
            .menuId(16)
            .menuName(null)
            .title("title")
            .dateCreated(LocalDateTime.now())
            .build();

    // when
    Set<ConstraintViolation<MyBoardsDto>> violations = validator.validate(myBoardsDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
