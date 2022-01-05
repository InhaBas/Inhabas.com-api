package com.inhabas.api.domain;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoardTest {
    public static final NormalBoard FREE_BOARD = new NormalBoard("이건 제목", "이건 내용입니다.", MEMBER1, Category.free);
    public static final NormalBoard NOTICE_BOARD = new NormalBoard("이건 공지", "이건 공지입니다.", MEMBER1, Category.notice);
    public static final NormalBoard NOTICE_BOARD_2 = new NormalBoard("이건 공지2", "이건 공지2입니다.", MEMBER1, Category.notice);

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
        assertThrows(IllegalArgumentException.class,
                () -> new Title(titleString)
        );
    }

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
}
