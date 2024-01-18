package com.inhabas.api.domain.board.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        SaveBoardDto saveBoardDto = new SaveBoardDto("title", "content", new MenuId(1));

        //when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertThat(violations).isEmpty();
    }

    @DisplayName("SaveBoardDto의 content 필드가 null 이면 validation 실패")
    @Test
    public void Contents_is_null() {
        // given
        SaveBoardDto saveBoardDto = new SaveBoardDto("title", null, new MenuId(1));

        // when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
               .as("violations 컬렉션의 첫 번째 요소의 메시지가 \"본문을 입력하세요.\"와 동일해야 합니다.")
               .isEqualTo("본문을 입력하세요.");
    }

    @DisplayName("게시글의 제목이 100자 이상을 넘긴 경우 validation 통과하지 못함.")
    @Test
    public void Title_is_too_long() {
        //given
        String title = "title".repeat(20) + ".";
        String contents = "그냥 본문 내용입니다.";

        SaveBoardDto saveBoardDto = new SaveBoardDto(title, contents, new MenuId(1));

        // when
        Set<ConstraintViolation<SaveBoardDto>> violations = validator.validate(saveBoardDto);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
               .as("violations 컬렉션의 첫 번째 요소의 메시지가 \"제목은 최대 100자입니다.\"와 동일해야 합니다.")
               .isEqualTo("제목은 최대 100자입니다.");
    }

}
