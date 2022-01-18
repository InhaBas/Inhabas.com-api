package com.inhabas.api.dto.board;
import org.junit.jupiter.api.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SaveBoardDtoTest {

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

    @DisplayName("SaveBoardDto 객체를 정상적으로 생성한다.")
    @Test
    public void SaveBoardDto_is_OK(){
        //given
        String title = "이것은 제목";
        String contents = "이것은 내용입니다.";
        Integer category_id = 2;

        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle(title);
        saveBoardDto.setContents(contents);
        saveBoardDto.setCategory_id(category_id);

        //when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertTrue(violations.isEmpty());
    }

    @DisplayName("SaveBoardDto의 contents 필드가 null 상태이다.")
    @Test
    public void Contents_is_null() {
        // given
        String title = "이것은 제목";
        String contents = null;
        Integer category_id = 2;

        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle(title);
        saveBoardDto.setContents(contents);
        saveBoardDto.setCategory_id(category_id);

        // when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertFalse(violations.isEmpty());
    }


}
