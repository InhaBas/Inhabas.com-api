package com.inhabas.api.dto.board;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SaveBoardDtoTest {

    private static final Logger logger = LoggerFactory.getLogger(SaveBoardDtoTest.class);

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
<<<<<<< HEAD

        SaveBoardDto saveBoardDto = new SaveBoardDto(title, contents);
=======
        Integer category_id = 2;
=======
        Integer categoryId = 2;
>>>>>>> 7d702c0 ([refactory] getBoardList 수정, Dto Test 일부 수정)

        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle(title);
        saveBoardDto.setContents(contents);
<<<<<<< HEAD
        saveBoardDto.setCategory_id(category_id);
>>>>>>> 0cfe72f ([feature] Bean Validation Test 추가)
=======
        saveBoardDto.setCategoryId(categoryId);
>>>>>>> 7d702c0 ([refactory] getBoardList 수정, Dto Test 일부 수정)

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
<<<<<<< HEAD

        SaveBoardDto saveBoardDto = new SaveBoardDto(title, contents);
=======
        Integer category_id = 2;
=======
        Integer categoryId = 2;
>>>>>>> 7d702c0 ([refactory] getBoardList 수정, Dto Test 일부 수정)

        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle(title);
        saveBoardDto.setContents(contents);
<<<<<<< HEAD
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
=======
        saveBoardDto.setCategoryId(categoryId);
>>>>>>> 7d702c0 ([refactory] getBoardList 수정, Dto Test 일부 수정)

        // when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
<<<<<<< HEAD
        assertEquals("제목은 최대 100자입니다.", violations.iterator().next().getMessage());
=======
>>>>>>> 0cfe72f ([feature] Bean Validation Test 추가)
=======
        assertEquals("본문을 입력하세요.", violations.iterator().next().getMessage());

        for(ConstraintViolation<SaveBoardDto> violation : violations){
            logger.debug("violation error message : {}", violation.getMessage());
        }
>>>>>>> 7d702c0 ([refactory] getBoardList 수정, Dto Test 일부 수정)
    }

    @DisplayName("게시글의 제목이 100자 이상을 넘긴 경우 예외 처리")
    @Test
    void

}
