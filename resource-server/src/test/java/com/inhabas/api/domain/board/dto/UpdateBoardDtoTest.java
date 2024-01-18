package com.inhabas.api.domain.board.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UpdateBoardDtoTest {

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

    @DisplayName("UpdateBoardDto를 정상적으로 생성한다. ")
    @Test
    public void UpdateBoardDto_is_OK() {
        //given
        UpdateBoardDto updateBoardDto = new UpdateBoardDto(1, "title", "content");

        // when
        Set<ConstraintViolation<UpdateBoardDto>> violations = validator.validate(updateBoardDto);

        // then
        assertThat(violations).hasSize(0);
    }

    @DisplayName("본문에 공백이 입력되었을 경우 테스트를 통과하지 못함.")
    @Test
    public void Contents_is_empty() {
        //given
        UpdateBoardDto updateBoardDto = new UpdateBoardDto(2, "title", " ");

        // when
        Set<ConstraintViolation<UpdateBoardDto>> violations = validator.validate(updateBoardDto);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
               .as("violations 컬렉션의 첫 번째 요소의 메시지가 \"본문을 입력하세요.\"와 동일해야 합니다.")
               .isEqualTo("본문을 입력하세요.");
    }
}
