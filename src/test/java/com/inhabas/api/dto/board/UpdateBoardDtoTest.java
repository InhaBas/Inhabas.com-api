package com.inhabas.api.dto.board;

import org.hibernate.sql.Update;
import org.junit.jupiter.api.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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
        Integer id = 1;
        String title = "이것은 제목";
        String contents = "이것은 내용입니다.";
        Integer categoryId = 2;

        UpdateBoardDto updateBoardDto = new UpdateBoardDto();
        updateBoardDto.setId(id);
        updateBoardDto.setTitle(title);
        updateBoardDto.setContents(contents);
        updateBoardDto.setCategoryId(categoryId);

        // when
        Set<ConstraintViolation<UpdateBoardDto>> violations = validator.validate(updateBoardDto);

        // then
        assertTrue(violations.isEmpty());

    }
}
