package com.inhabas.api.dto.board;

import org.junit.jupiter.api.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

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
        UpdateBoardDto updateBoardDto = new UpdateBoardDto(1, "title", "contents", 12201863);

        // when
        Set<ConstraintViolation<UpdateBoardDto>> violations = validator.validate(updateBoardDto);

        // then
        assertEquals(0, violations.size());
    }

    @DisplayName("본문에 공백이 입력되었을 경우 테스트를 통과하지 못함.")
    @Test
    public void Contents_is_empty() {
        //given
        UpdateBoardDto updateBoardDto = new UpdateBoardDto(2, "title", " ", 12201863);

        // when
        Set<ConstraintViolation<UpdateBoardDto>> violations = validator.validate(updateBoardDto);

        // then
        assertEquals(1, violations.size());
        assertEquals("본문을 입력하세요", violations.iterator().next().getMessage());
    }
}
