package com.inhabas.api.domain.scholarship.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.entity.MemberTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScholarshipBoardDetailDtoTest {

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
  public void ScholarshipBoardDetailDto_is_OK() {
    // given
    Member writer = MemberTest.chiefMember();
    ReflectionTestUtils.setField(writer, "id", 1L);
    ScholarshipBoardDetailDto scholarshipBoardDetailDto =
        new ScholarshipBoardDetailDto(
            1L,
            "title",
            "content",
            writer,
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            new ArrayList<>(),
            new ArrayList<>());

    // when
    Set<ConstraintViolation<ScholarshipBoardDetailDto>> violations =
        validator.validate(scholarshipBoardDetailDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("ScholarshipBoardDetailDto 각각의 필드가 null 이면 validation 실패")
  @Test
  public void Content_is_null() {
    // given
    Member writer = MemberTest.chiefMember();
    ReflectionTestUtils.setField(writer, "id", 1L);
    ScholarshipBoardDetailDto scholarshipBoardDetailDto =
        new ScholarshipBoardDetailDto(null, null, null, writer, null, null, null, null, null);

    // when
    Set<ConstraintViolation<ScholarshipBoardDetailDto>> violations =
        validator.validate(scholarshipBoardDetailDto);

    // then
    assertThat(violations).hasSize(9);
  }
}
