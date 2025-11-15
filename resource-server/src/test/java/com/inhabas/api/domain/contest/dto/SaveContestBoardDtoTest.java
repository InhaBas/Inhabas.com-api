package com.inhabas.api.domain.contest.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SaveContestBoardDtoTest {
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

  @DisplayName("공모전 게시글 저장 시 모든 필드가 null일 경우")
  @Test
  public void FieldsAreNullError() {
    // given
    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(null)
            .title(null)
            .content(null)
            .association(null)
            .topic(null)
            .dateContestStart(null)
            .dateContestEnd(null)
            .files(null)
            .build();

    // when
    Set<ConstraintViolation<SaveContestBoardDto>> violations =
        validator.validate(saveContestBoardDto);

    // then
    List<String> errorMessage = new ArrayList<>();
    violations.forEach(error -> errorMessage.add(error.getMessage()));

    Assertions.assertThat(errorMessage)
        .contains(
            "제목을 입력하세요.",
            "본문을 입력하세요.",
            "협회기관을 입력하세요.",
            "공모전 주제를 입력하세요.",
            "공모전 모집 시작일을 등록해주세요.",
            "공모전 모집 마감일을 등록해주세요.");
  }

  @DisplayName("공모전 게시글 저장 시 모든 필드가 Blank일 경우")
  @Test
  public void FieldsAreBlankedError() {
    // given
    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(null)
            .title(" ")
            .content(" ")
            .association(" ")
            .topic(" ")
            .dateContestStart(null)
            .dateContestEnd(null)
            .files(Arrays.asList())
            .build();

    // when
    Set<ConstraintViolation<SaveContestBoardDto>> violations =
        validator.validate(saveContestBoardDto);

    // then
    List<String> errorMessage = new ArrayList<>();
    violations.forEach(error -> errorMessage.add(error.getMessage()));

    Assertions.assertThat(errorMessage)
        .contains(
            "제목을 입력하세요.",
            "본문을 입력하세요.",
            "협회기관을 입력하세요.",
            "공모전 주제를 입력하세요.",
            "공모전 모집 시작일을 등록해주세요.",
            "공모전 모집 마감일을 등록해주세요.");
  }

  @DisplayName("공모전 게시글 저장 시 제목, 협회기관명, 주제가 입력 길이를 초과하여 Validation 실패")
  @Test
  public void InputsAreExceededError() {
    // given
    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(1L)
            .title("title".repeat(20) + ".")
            .content(
                "content! Cucumber paste has to have a sun-dried, chilled sauerkraut component.")
            .association("Assoc".repeat(20) + ".")
            .topic("topic".repeat(100) + ".")
            .dateContestStart(LocalDateTime.of(2022, 1, 1, 0, 0, 0))
            .dateContestEnd(LocalDateTime.of(9999, 3, 3, 0, 0, 0))
            .files(Arrays.asList("fileId"))
            .build();

    // when
    Set<ConstraintViolation<SaveContestBoardDto>> violations =
        validator.validate(saveContestBoardDto);

    // then
    List<String> errorMessage = new ArrayList<>();
    violations.forEach(error -> errorMessage.add(error.getMessage()));

    Assertions.assertThat(violations).hasSize(3);
    Assertions.assertThat(errorMessage)
        .containsOnly("제목은 최대 100자입니다.", "100자 이내로 작성해주세요.", "500자 이내로 작성해주세요.");
  }

  @DisplayName("공모전 게시글 저장 시 마감일자가 이미 지난 경우 Validation 실패")
  @Test
  public void DeadlineIsOutdatedError() {
    // given
    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(1L)
            .title("title")
            .content("content")
            .association("association")
            .topic("topic")
            .dateContestStart(LocalDateTime.of(2022, 1, 1, 0, 0, 0))
            .dateContestEnd(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
            .files(Arrays.asList("fileId"))
            .build();

    // when
    Set<ConstraintViolation<SaveContestBoardDto>> violations =
        validator.validate(saveContestBoardDto);

    // then
    List<String> errorMessage = new ArrayList<>();
    violations.forEach(error -> errorMessage.add(error.getMessage()));

    Assertions.assertThat(errorMessage).containsOnly("이미 모집기간이 종료된 공모전은 등록할 수 없습니다.");
  }
}
