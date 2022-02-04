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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static com.inhabas.api.dto.contest.BaseContestBoardDtoTest.updateContestBoardDto1;
import static com.inhabas.api.dto.contest.BaseContestBoardDtoTest.updateContestBoardDto2;


public class UpdateContestBoardDtoTest {
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

    @DisplayName("UpdateContestBoardDto를 정상적으로 생성한다.")
    @Test
    public void UpdateContestBoardDto_is_OK() {
        // when
        Set<ConstraintViolation<UpdateContestBoardDto>> violations = validator.validate(updateContestBoardDto1);

        // then
        assertEquals(0, violations.size());
    }

    @DisplayName("본문에 공백이 입력되었을 경우 테스트를 통과하지 못함.")
    @Test
    public void Contents_is_empty() {
        // when
        Set<ConstraintViolation<UpdateContestBoardDto>> violations = validator.validate(updateContestBoardDto2);

        // then
        assertEquals(1, violations.size());
        assertEquals("본문을 입력하세요.", violations.iterator().next().getMessage());
    }
}
