package com.inhabas.api.domain.wrapper;

import com.inhabas.api.domain.menu.wrapper.MenuName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MenuNameTest {

    @DisplayName("MenuName 타입에 메뉴이름 저장")
    @Test
    public void MenuName_is_OK() {
        //given
        String menuName = "게시판";

        //when
        MenuName name = new MenuName(menuName);

        //then
        assertThat(name.getValue()).isEqualTo("게시판");
    }

    @DisplayName("MenuName 타입에 너무 긴 메뉴이름 저장 시도. 15자 이상")
    @Test
    public void MenuName_is_too_long() {
        //given
        String menuName = "게시판".repeat(5); // 15자

        //when
        assertThrows(
                IllegalArgumentException.class,
                ()-> new MenuName(menuName)
        );
    }

    @DisplayName("메뉴이름은 null 일 수 없다.")
    @Test
    public void MenuName_cannot_be_null() {
        assertThrows(IllegalArgumentException.class,
                ()-> new MenuName(null));
    }

    @DisplayName("메뉴이름은 빈 문자열일 수 없다.")
    @Test
    public void MenuName_cannot_be_blank() {
        assertThrows(IllegalArgumentException.class,
                ()-> new MenuName(" "));
    }
}
