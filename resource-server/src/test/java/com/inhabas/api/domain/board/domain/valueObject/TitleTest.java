package com.inhabas.api.domain.board.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TitleTest {

    @DisplayName("Title 타입에 제목을 저장한다.")
    @Test
    public void Title_is_OK() {
        //given
        String  titleString = "게시판 제목입니다.";

        //when
        Title title = new Title(titleString);

        //then
        assertThat(title.getValue()).isEqualTo("게시판 제목입니다.");
    }

    @DisplayName("Title 타입에 너무 긴 제목을 저장한다. 100자 이상")
    @Test
    public void Title_is_too_long() {
        //given
        String titleString = "지금이문장은10자임".repeat(10);

        //then
        assertThatThrownBy(() -> new Title(titleString))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
    }

    @DisplayName("제목은 null 일 수 없습니다.")
    @Test
    public void Title_cannot_be_Null() {
        assertThatThrownBy(() -> new Title(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
    }

    @DisplayName("제목은 빈 문자열일 수 없습니다.")
    @Test
    public void Title_cannot_be_Blank() {
        assertThatThrownBy(() -> new Title(""))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("입력값이 없거나, 타입이 유효하지 않습니다.");
    }
}
