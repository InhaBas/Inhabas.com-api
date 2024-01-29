package com.inhabas.api.domain.comment.domain;

import static org.mockito.Mockito.mock;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class CommentTest {

  private Comment rootComment;

  @BeforeEach
  void setUp() {
    rootComment = new Comment("content", mock(Member.class), mock(BaseBoard.class));
  }

  @DisplayName("생성자로 객체를 생성한다.")
  @Test
  void constructorTest() {
    // given
    String content = "content";
    Member member = MemberTest.chiefMember();
    BaseBoard baseBoard = mock(BaseBoard.class);

    // when
    Comment comment = new Comment(content, member, baseBoard);

    // then
    Assertions.assertThat(comment.getContent()).isEqualTo(content);
  }

  @DisplayName("작성자가 댓글 내용을 수정한다.")
  @Test
  void updateTest() {
    // given
    String content = "content";
    Member member = MemberTest.chiefMember();
    ReflectionTestUtils.setField(member, "id", 1L);
    BaseBoard baseBoard = mock(BaseBoard.class);

    Comment comment = new Comment(content, member, baseBoard);

    // when
    final String newContent = "new content";
    comment.update(newContent);

    // then
    Assertions.assertThat(comment.getContent()).isEqualTo(newContent);
  }

  @DisplayName("rootComment 의 대댓글을 작성한다.")
  @Test
  void replyToTest() {
    // given
    String content = "content";
    Member member = MemberTest.chiefMember();
    BaseBoard baseBoard = mock(BaseBoard.class);
    Comment comment = new Comment(content, member, baseBoard);

    // when
    comment.replyTo(rootComment);

    // then
    Assertions.assertThat(rootComment.getChildrenComment()).contains(comment);
  }

  @DisplayName("작성자와의 id와 동일한지 확인한다.")
  @Test
  void isWrittenByTest() {
    // given
    String content = "content";
    Member member = MemberTest.chiefMember();
    BaseBoard baseBoard = mock(BaseBoard.class);
    ReflectionTestUtils.setField(member, "id", 1L);
    Comment comment = new Comment(content, member, baseBoard);

    // when
    boolean mustTrue = comment.isWrittenBy(1L);

    // then
    Assertions.assertThat(mustTrue).isTrue();
  }
}
