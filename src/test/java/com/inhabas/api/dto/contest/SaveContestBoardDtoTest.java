package com.inhabas.api.dto.contest;

import com.inhabas.api.dto.board.SaveBoardDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static com.inhabas.api.dto.contest.BaseContestBoardDtoTest.*;



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

    @DisplayName("SaveContestBoardDto 객체를 정상적으로 생성한다. ")
    @Test
    public void SaveContestBoardDto_is_OK() {
        // when
        Set<ConstraintViolation<SaveContestBoardDto>> violations = validator.validate(saveContestBoardDto1);

        // then
        assertTrue(violations.isEmpty());
    }

    @DisplayName("SaveContestBoardDto의 contents 필드가 null 이면 validation 실패")
    @Test
    public void Contents_is_null() {
        // when
        Set<ConstraintViolation<SaveContestBoardDto>> violations = validator.validate(saveContestBoardDto2);

        // then
        assertEquals(1, violations.size());
        assertEquals("본문을 입력하세요.", violations.iterator().next().getMessage());
    }

    @DisplayName("게시글의 제목이 100자 이상을 넘긴 경우 validation 통과하지 못함.")
    @Test
    public void Title_is_too_long() {
        // when
        Set<ConstraintViolation<SaveContestBoardDto>> violations = validator.validate(saveContestBoardDto3);

        // then
        assertEquals(1, violations.size());
        assertEquals("제목은 최대 100자입니다.", violations.iterator().next().getMessage());
    }
}
