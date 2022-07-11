package com.inhabas.api.domain.budget.valueObject;

import com.inhabas.api.domain.budget.domain.valueObject.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TitleTest {

    @DisplayName("Title 타입에 제목을 저장한다.")
    @Test
    public void Title_is_OK() {
        //given
        String  titleString = "예산 내역 제목입니다.";

        //when
        Title title = new Title(titleString);

        //then
        assertThat(title.getValue()).isEqualTo("예산 내역 제목입니다.");
    }

    @DisplayName("Title 타입에 너무 긴 제목을 저장한다. 100자 이상")
    @Test
    public void Title_is_too_long() {
        //given
        String titleString = "지금이문장은10자임".repeat(10);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> new Title(titleString));
    }

    @DisplayName("예산 내역 제목은 null 일 수 없습니다.")
    @Test
    public void Title_cannot_be_Null() {
        assertThrows(IllegalArgumentException.class,
                () -> new Title(null));
    }

    @DisplayName("예산 내역 제목은 빈 문자열일 수 없습니다.")
    @Test
    public void Title_cannot_be_Blank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Title("\t"));
    }
}
