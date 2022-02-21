package com.inhabas.api.dto.contest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UpdateContestBoardDtoTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private static List<String> errorMessage;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

        errorMessage = new ArrayList<>();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @DisplayName("공모전 게시글 수정 시 모든 필드가 null일 경우 테스트를 통과하지 못함.")
    @Test
    public void FieldsAreNullError() {
        // given
        UpdateContestBoardDto updateContestBoardDto = new UpdateContestBoardDto(
            null, null, null, null, null, null, null, null );

        // when
        Set<ConstraintViolation<UpdateContestBoardDto>> violations = validator.validate(updateContestBoardDto);
        violations.forEach(error -> errorMessage.add(error.getMessage()));

        // then
        assertThat(errorMessage).contains(
                "수정할 게시글을 선택해주세요.",
                "제목을 입력하세요.",
                "본문을 입력하세요.",
                "협회기관을 입력하세요.",
                "공모전 주제를 입력하세요.",
                "공모전 모집 시작일을 등록해주세요.",
                "공모전 모집 마감일을 등록해주세요."
        );
    }

    @DisplayName("공모전 게시글 수정 시 공백이 입력되었을 경우 테스트를 통과하지 못함.")
    @Test
    public void FieldsAreBlankError() {
        // given
        UpdateContestBoardDto updateContestBoardDto = new UpdateContestBoardDto(
                1, " ", " ", " ", " ", LocalDate.of(2022, 01, 01), LocalDate.of(2023, 02, 10), 12201863);

        // when
        Set<ConstraintViolation<UpdateContestBoardDto>> violations = validator.validate(updateContestBoardDto);
        violations.forEach(error -> errorMessage.add(error.getMessage()));

        //then
        assertThat(errorMessage).contains(
                "제목을 입력하세요.",
                "본문을 입력하세요.",
                "협회기관을 입력하세요.",
                "공모전 주제를 입력하세요."
        );
    }

    @DisplayName("공모전 게시글 수정 시 제목, 협회기관명, 주제가 입력 길이를 초과하여 Validation 실패")
    @Test
    public void  InputsAreExceededError() {
        //given
        UpdateContestBoardDto updateContestBoardDto = new UpdateContestBoardDto(
                1,
                "title".repeat(20) + ".",
                "contents! Cucumber paste has to have a sun-dried, chilled sauerkraut component.",
                "Assoc".repeat(20) + ".",
                "topic".repeat(100)+ ".",
                LocalDate.of(2022, 01, 01),
                LocalDate.of(2022, 03, 03),
                12201863
        );

        // when
        Set<ConstraintViolation<UpdateContestBoardDto>> violations = validator.validate(updateContestBoardDto);
        violations.forEach(error -> errorMessage.add(error.getMessage()));

        // then
        assertEquals(3, violations.size());
        assertThat(errorMessage).contains(
                "제목은 최대 100자입니다.",
                "100자 이내로 작성해주세요.",
                "500자 이내로 작성해주세요."
        );
    }
}
