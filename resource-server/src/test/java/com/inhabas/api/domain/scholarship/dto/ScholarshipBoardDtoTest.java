package com.inhabas.api.domain.scholarship.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.entity.MemberTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScholarshipBoardDtoTest {

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

  @DisplayName("ScholarshipBoardDto 객체를 정상적으로 생성한다.")
  @Test
  public void ScholarshipBoardDto_is_OK() {
    // given
    Member writer = MemberTest.chiefMember();
    ReflectionTestUtils.setField(writer, "id", 1L);
    ScholarshipBoardDto scholarshipBoardDto =
        new ScholarshipBoardDto(1L, "title", writer, LocalDateTime.now(), LocalDateTime.now());

    // when
    Set<ConstraintViolation<ScholarshipBoardDto>> violations =
        validator.validate(scholarshipBoardDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("ScholarshipBoardDto 각각의 필드가 null 이면 validation 실패")
  @Test
  public void Content_is_null() {
    // given
    Member writer = MemberTest.chiefMember();
    ReflectionTestUtils.setField(writer, "id", 1L);
    ScholarshipBoardDto scholarshipBoardDto =
        new ScholarshipBoardDto(null, null, writer, null, null);

    // when
    Set<ConstraintViolation<ScholarshipBoardDto>> violations =
        validator.validate(scholarshipBoardDto);

    // then
    assertThat(violations).hasSize(4);
  }
}
