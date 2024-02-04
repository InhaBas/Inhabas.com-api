package com.inhabas.api.domain.comment.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.AlbumBoard;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.AlbumExampleTest;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.repository.CommentRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuExampleTest;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

  @Mock private CommentRepository commentRepository;
  @Mock private EntityManager em;

  @InjectMocks private CommentServiceImpl commentService;

  private Member proxyWriter;
  private Menu proxyMenu;
  private MenuGroup proxyMenuGroup;
  private AlbumBoard proxyBoard;

  @BeforeEach
  public void setUpMocking() {
    proxyWriter = MemberTest.getTestBasicMember("12171652");
    ReflectionTestUtils.setField(proxyWriter, "id", 1L);
    proxyMenuGroup = MenuGroupExampleTest.getMenuGroup1();
    ReflectionTestUtils.setField(proxyMenuGroup, "id", 1);
    proxyMenu = MenuExampleTest.getMenu1(proxyMenuGroup);
    ReflectionTestUtils.setField(proxyMenu, "id", 1);
    proxyBoard = AlbumExampleTest.getAlbumBoard1(proxyMenu);
    ReflectionTestUtils.setField(proxyBoard, "id", 1L);
  }

  @DisplayName("새로운 댓글을 저장한다.")
  @Test
  public void SaveNewCommentTest() {

    // mocking
    Comment comment = new Comment("이야 이게 댓글 기능이라고??", proxyWriter, proxyBoard);
    ReflectionTestUtils.setField(comment, "id", 1L);
    given(em.getReference(eq(Member.class), any())).willReturn(proxyWriter);
    given(em.getReference(eq(BaseBoard.class), any())).willReturn(proxyBoard);
    given(commentRepository.save(any(Comment.class))).willReturn(comment);

    // given
    CommentSaveDto newCommentCreateRequest = new CommentSaveDto("이야 이게 댓글 기능이라고??", null);

    // when
    Long returnId =
        commentService.create(
            newCommentCreateRequest, proxyMenu.getId(), proxyBoard.getId(), proxyWriter.getId());

    // then
    assertThat(returnId).isNotNull();
    verify(commentRepository, times(1)).save(any(Comment.class));
  }

  @DisplayName("대댓글을 성공적으로 등록한다.")
  @Test
  public void createReply() {
    // mocking
    Comment parentComment = new Comment("댓글이 잘 써지네요", proxyWriter, proxyBoard);
    Comment reply = new Comment("이야 이게 댓글 기능이라고??", proxyWriter, proxyBoard);
    ReflectionTestUtils.setField(parentComment, "id", 1L);
    ReflectionTestUtils.setField(reply, "id", 2L);
    given(em.getReference(eq(Member.class), any())).willReturn(proxyWriter);
    given(em.getReference(eq(BaseBoard.class), any())).willReturn(proxyBoard);
    given(em.getReference(eq(Comment.class), any())).willReturn(parentComment);
    given(commentRepository.save(any(Comment.class))).willReturn(reply);

    // given
    CommentSaveDto newCommentCreateRequest = new CommentSaveDto("이야 이게 댓글 기능이라고??", 1L);

    // when
    Long returnId =
        commentService.create(
            newCommentCreateRequest, proxyMenu.getId(), proxyBoard.getId(), proxyWriter.getId());

    // then
    assertThat(returnId).isEqualTo(2);
    verify(commentRepository, times(1)).save(any(Comment.class));
  }

  @DisplayName("댓글을 성공적으로 수정한다.")
  @Test
  public void UpdateCommentTest() {
    // mocking
    Long commentId = 1L;
    given(commentRepository.findById(commentId))
        .willReturn(expectedCommentAfterFind(commentId, proxyWriter, proxyBoard));

    // given
    CommentUpdateDto commentUpdateDto = new CommentUpdateDto("내용 수정 좀 할게요.");

    // when
    Long returnId = commentService.update(commentId, commentUpdateDto);

    // then
    assertThat(returnId).isNotNull();
  }

  private Optional<Comment> expectedCommentAfterFind(
      Long commentId, Member proxyWriter, BaseBoard proxyBoard) {
    Comment comment = new Comment("이야 이게 댓글 기능이라고??", proxyWriter, proxyBoard);
    ReflectionTestUtils.setField(comment, "id", commentId);

    return Optional.of(comment);
  }

  @DisplayName("댓글 리스트를 찾는 메소드를 호출한다.")
  @Test
  public void getComments() {
    // when
    commentService.getComments(proxyBoard.getMenu().getId(), proxyBoard.getId());

    // then
    verify(commentRepository, times(1)).findAllByParentBoardIdOrderByCreated(proxyBoard.getId());
  }

  @DisplayName("댓글을 성공적으로 삭제한다.")
  @Test
  public void deleteComment() {
    // given
    given(commentRepository.findById(anyLong()))
        .willReturn(expectedCommentAfterFind(1L, proxyWriter, proxyBoard));

    // when then
    assertThatCode(() -> commentService.delete(1L)).doesNotThrowAnyException();
  }
}
