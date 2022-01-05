package com.inhabas.api.domain;

import com.inhabas.api.domain.comment.type.wrapper.Contents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentTest {

    @DisplayName("Contents 타입에 내용을 저장한다.")
    @Test
    public void Contents_is_saved_well() {
        //given
        String contentsString = "날씨 너무 좋지 않아? 개발하기 딱 좋은 날씨야! 같이 개발할래? 야 너두 할 수 있어";

        //when
        Contents contents = new Contents(contentsString);

        //then
        assertThat(contents.getValue()).isEqualTo(contentsString);
    }

    @DisplayName("Contents 타입에 너무 긴 내용을 입력한다. 500자 이상")
    @Test
    public void Contents_is_too_long() {
        //given
        String contentsString = "지금이문장은10자임".repeat(500);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> new Contents(contentsString)
        );
    }

}
