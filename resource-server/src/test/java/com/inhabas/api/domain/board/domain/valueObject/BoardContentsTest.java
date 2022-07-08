package com.inhabas.api.domain.board.domain.valueObject;

import com.inhabas.api.domain.board.domain.valueObject.Contents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardContentsTest {

    @DisplayName("Contents 타입에 게시글 내용을 저장한다.")
    @Test
    public void Contents_is_OK() {
        String contentsString = ".".repeat(2 << 24 - 2); // 16 MB - 2 byte

        Contents contents = new Contents(contentsString);

        assertThat(contents.getValue()).isEqualTo(contentsString);
    }

    @DisplayName("Contents 타입에 너무 긴 게시글을 저장한다. (16 MB - 1 byte) 이상")
    @Test
    public void Contents_is_too_long() {
        String contentsString = ".".repeat(2 << 24 - 1); // 16 MB - 1 byte

        //then
        assertThrows(IllegalArgumentException.class,
                () -> new Contents(contentsString));
    }

    @DisplayName("Contents 타입에 공백을 저장할 수 없다.")
    @Test
    public void Contents_is_Empty() {
        assertThrows(IllegalArgumentException.class,
                () -> new Contents(""));
    }

    @DisplayName("Contents 타입에 null 은 허용 안된다.")
    @Test
    public void Contents_is_Null() {
        assertThrows(IllegalArgumentException.class,
                () -> new Contents(null));
    }
}
