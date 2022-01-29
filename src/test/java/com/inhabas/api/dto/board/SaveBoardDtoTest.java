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
<<<<<<< HEAD

        SaveBoardDto saveBoardDto = new SaveBoardDto(title, contents);
=======
        Integer category_id = 2;

        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle(title);
        saveBoardDto.setContents(contents);
        saveBoardDto.setCategory_id(category_id);
>>>>>>> 0cfe72f ([feature] Bean Validation Test 추가)

        //when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertTrue(violations.isEmpty());
    }

<<<<<<< HEAD
    @DisplayName("SaveBoardDto의 contents 필드가 null 이면 validation 실패")
=======
    @DisplayName("SaveBoardDto의 contents 필드가 null 상태이다.")
>>>>>>> 0cfe72f ([feature] Bean Validation Test 추가)
    @Test
    public void Contents_is_null() {
        // given
        String title = "이것은 제목";
        String contents = null;
<<<<<<< HEAD

        SaveBoardDto saveBoardDto = new SaveBoardDto(title, contents);
=======
        Integer category_id = 2;

        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle(title);
        saveBoardDto.setContents(contents);
        saveBoardDto.setCategory_id(category_id);
>>>>>>> 0cfe72f ([feature] Bean Validation Test 추가)

        // when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertFalse(violations.isEmpty());
<<<<<<< HEAD
        assertEquals(1, violations.size());
        assertEquals("본문을 입력하세요.", violations.iterator().next().getMessage());
    }

    @DisplayName("게시글의 제목이 100자 이상을 넘기면 validation 실패")
    @Test
    public void Title_is_too_long() {
        //given
        String title = "title".repeat(30);
        String contents = "그냥 본문 내용입니다.";

        SaveBoardDto saveBoardDto = new SaveBoardDto(title, contents);

        // when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("제목은 최대 100자입니다.", violations.iterator().next().getMessage());
=======
>>>>>>> 0cfe72f ([feature] Bean Validation Test 추가)
    }


}
