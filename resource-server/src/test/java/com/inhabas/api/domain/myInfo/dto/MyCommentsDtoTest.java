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

public class MyCommentsDtoTest {

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

  @DisplayName("MyCommentsDto 객체를 정상적으로 생성한다.")
  @Test
  public void MyCommentsDto_is_OK() {
    // given
    MyCommentsDto myCommentsDto =
        MyCommentsDto.builder()
            .id(1L)
            .menuId(16)
            .menuName("알파 테스터")
            .content("댓글 내용")
            .dateCreated(LocalDateTime.now())
            .build();

    // when
    Set<ConstraintViolation<MyCommentsDto>> violations = validator.validate(myCommentsDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("MyCommentsDto content 필드가 blank 이면 validation 실패")
  @Test
  public void Content_is_blank() {
    // given
    MyCommentsDto myCommentsDto =
        MyCommentsDto.builder()
            .id(1L)
            .menuId(16)
            .menuName("알파 테스터")
            .content("")
            .dateCreated(LocalDateTime.now())
            .build();

    // when
    Set<ConstraintViolation<MyCommentsDto>> violations = validator.validate(myCommentsDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
