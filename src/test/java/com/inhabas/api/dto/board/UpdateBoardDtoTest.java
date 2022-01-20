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
<<<<<<< HEAD
=======
        assertEquals(0, violations.size());
>>>>>>> f9c4bbf ([bugfix] GitHub Comments 반영)
    }

    @DisplayName("본문에 공백이 입력되었을 경우 + CategoryId가 등록되지 않았을 경우 통과하지 못함")
    @Test
    public void Contents_is_empty() {
        //given
        Integer id = 2;
        String title = "이것은 제목";
        String contents = " ";
        Integer categoryId = null;

        UpdateBoardDto updateBoardDto = new UpdateBoardDto(id, title, contents);

        // when
        Set<ConstraintViolation<UpdateBoardDto>> violations = validator.validate(updateBoardDto);


        // then
        assertFalse(violations.isEmpty());
<<<<<<< HEAD
        assertEquals(1, violations.size());

=======
        assertEquals(2, violations.size());
>>>>>>> f9c4bbf ([bugfix] GitHub Comments 반영)
    }
}
