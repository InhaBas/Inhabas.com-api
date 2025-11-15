package com.inhabas.api.domain.myInfo.dto;

import static com.inhabas.api.domain.menu.domain.valueObject.MenuType.ALPHA;
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

public class MyCommentDtoTest {

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

  @DisplayName("MyCommentDto 객체를 정상적으로 생성한다.")
  @Test
  public void MyCommentsDto_is_OK() {
    // given
    MyCommentDto myCommentDto =
        MyCommentDto.builder()
            .id(1L)
            .menuId(16)
            .menuType(ALPHA)
            .menuName("알파 테스터")
            .content("댓글 내용")
            .isDeleted(Boolean.FALSE)
            .dateCreated(LocalDateTime.now())
            .build();

    // when
    Set<ConstraintViolation<MyCommentDto>> violations = validator.validate(myCommentDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("MyCommentDto content 필드가 blank 이면 validation 실패")
  @Test
  public void Content_is_blank() {
    // given
    MyCommentDto myCommentDto =
        MyCommentDto.builder()
            .id(1L)
            .menuId(16)
            .menuType(ALPHA)
            .menuName("알파 테스터")
            .content("")
            .isDeleted(Boolean.FALSE)
            .dateCreated(LocalDateTime.now())
            .build();

    // when
    Set<ConstraintViolation<MyCommentDto>> violations = validator.validate(myCommentDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
