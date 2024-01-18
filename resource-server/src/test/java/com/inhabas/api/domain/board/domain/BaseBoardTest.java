package com.inhabas.api.domain.board.domain;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.exception.WriterUnmodifiableException;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class BaseBoardTest {

    @Mock
    private Member member;
    @Mock
    private Menu menu;

    private BaseBoard baseBoard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String title = "title1";
        baseBoard = new BaseBoard(title, member, menu) {};

    }

    @Test
    void ConstructorTest() {
        //then
        assertThat("title1").isEqualTo(baseBoard.getTitle());
        assertThat(member).isEqualTo(baseBoard.writer);
        assertThat(menu).isEqualTo(baseBoard.menu);

    }

    @Test
    void writtenByWriterUnmodifiableTest() {
        //given
        Member newMember = mock(Member.class);

        //when, then
        assertThatThrownBy(() -> baseBoard.writtenBy(newMember, BaseBoard.class))
                .isInstanceOf(WriterUnmodifiableException.class)
                .hasMessage("글 작성자를 변경 할 수 없습니다.");

    }

    @Test
    void isWrittenByTest() {
        //given
        Member anotherMember = mock(Member.class);

        //when, then
        assertThat(baseBoard.isWrittenBy(anotherMember)).isFalse();

    }

    @Test
    void addFileTest() {
        //given
        BoardFile file = mock(BoardFile.class);

        //when
        baseBoard.addFile(file);

        //then
        assertThat(baseBoard.files.contains(file)).isTrue();

    }

    @Test
    void addCommentTest() {
        //given
        Comment comment = mock(Comment.class);

        //when
        baseBoard.addComment(comment);

        //then
        assertThat(baseBoard.comments.contains(comment)).isTrue();

    }
}