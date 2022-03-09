package com.inhabas.api.domain.wrapper;

import com.inhabas.api.domain.menu.wrapper.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuCategoryDescriptionTest {

    @DisplayName("게시판 설명으로 null 값은 허용 안됨.")
    @Test
    public void CategoryName_cannot_be_null() {
        //when
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Description(null));
    }

    @DisplayName("게시판 설명으로 빈 문자열은 허용 안됨.")
    @Test
    public void CategoryName_cannot_be_blank() {
        //when
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Description("  "));
    }

    @DisplayName("게시판 설명 50글자 이상은 허용 안됨.")
    @Test
    public void CategoryName_too_long() {
        //given
        String description = "이문장7글자임".repeat(7);

        //when : 49글자 정상 저장
        Description categoryName = new Description(description);
        assertThat(categoryName.getValue()).isEqualTo(description);

        //when : 50글자 저장 안됨.
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Description(description+"."));
    }
}
