package com.inhabas.api.domain.comment.domain.valueObject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class CommentContentTest {

    @DisplayName("Content 타입에 내용을 저장한다.")
    @Test
    public void Contents_is_saved_well() {
        //given
        String contentsString = "날씨 너무 좋지 않아? 개발하기 딱 좋은 날씨야! 같이 개발할래? 야 너두 할 수 있어";

        //when
        Contents contents = new Contents(contentsString);

        //then
        Assertions.assertThat(contents.getValue()).isEqualTo(contentsString);
    }

    @DisplayName("Content 타입에 너무 긴 내용을 입력한다. 500자 이상")
    @Test
    public void Contents_is_too_long() {
        //given
        String contentsString = "지금이문장은10자임".repeat(500);

        //then
        Assertions.assertThatThrownBy(() -> new Contents(contentsString))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }

    @DisplayName("Content 타입에 null 은 안된다.")
    @Test
    public void Contents_is_Null() {
        Assertions.assertThatThrownBy(() -> new Contents(null))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }

    @DisplayName("Content 타입에 공백댓글은 저장할 수 없다.")
    @Test
    public void Contents_is_Blank() {
        Assertions.assertThatThrownBy(() -> new Contents("  "))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }
}
