package com.inhabas.api.domain.comment.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.domain.NormalBoardTest;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.repository.CommentRepository;
import com.inhabas.api.domain.member.domain.MemberTest;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private EntityManager em;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Member proxyWriter;
    private NormalBoard proxyBoard;

    @BeforeEach
    public void setUpMocking() {
        proxyWriter = MemberTest.getTestMember(12171652);
        proxyBoard = NormalBoardTest.getTestBoard(12);
    }

    @DisplayName("새로운 댓글을 저장한다.")
    @Test
    public void SaveNewCommentTest() {

        //mocking
        Comment comment = new Comment("이야 이게 댓글 기능이라고??", proxyWriter, proxyBoard);
        ReflectionTestUtils.setField(comment, "id", 1);
        given(em.getReference(eq(Member.class), any())).willReturn(proxyWriter);
        given(em.getReference(eq(NormalBoard.class), any())).willReturn(proxyBoard);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        //given
        CommentSaveDto newCommentCreateRequest = new CommentSaveDto("이야 이게 댓글 기능이라고??", 12);

        //when
        Integer returnId = commentService.create(newCommentCreateRequest, proxyWriter.getId());

        //then
        assertThat(returnId).isNotNull();
        verify(commentRepository, times(1))
                .save(any(Comment.class));
    }

    @DisplayName("대댓글을 성공적으로 등록한다.")
    @Test
    public void createReply() {
        //mocking
        Comment parentComment = new Comment("댓글이 잘 써지네요", proxyWriter, proxyBoard);
        Comment reply = new Comment("이야 이게 댓글 기능이라고??", proxyWriter, proxyBoard);
        ReflectionTestUtils.setField(parentComment, "id", 1);
        ReflectionTestUtils.setField(reply, "id", 2);
        given(em.getReference(eq(Member.class), any())).willReturn(proxyWriter);
        given(em.getReference(eq(NormalBoard.class), any())).willReturn(proxyBoard);
        given(em.getReference(eq(Comment.class), any())).willReturn(parentComment);
        given(commentRepository.save(any(Comment.class))).willReturn(reply);

        //given
        CommentSaveDto newCommentCreateRequest = new CommentSaveDto("이야 이게 댓글 기능이라고??", 12, 1);

        //when
        Integer returnId = commentService.create(newCommentCreateRequest, proxyWriter.getId());

        //then
        assertThat(returnId).isEqualTo(2);
        verify(commentRepository, times(1))
                .save(any(Comment.class));
    }

    @DisplayName("댓글을 성공적으로 수정한다.")
    @Test
    public void UpdateCommentTest() {
        //mocking
        Integer commentId = 1;
        given(commentRepository.findById(commentId))
                .willReturn(expectedCommentAfterFind(commentId, proxyWriter, proxyBoard));

        //given
        CommentUpdateDto param = new CommentUpdateDto(1, "내용 수정 좀 할게요.");

        //when
        Integer returnId = commentService.update(param, new MemberId(12171652));

        //then
        assertThat(returnId).isNotNull();
    }

    private Optional<Comment> expectedCommentAfterFind(Integer commentId, Member proxyWriter, NormalBoard proxyBoard) {
        Comment comment = new Comment("이야 이게 댓글 기능이라고??", proxyWriter, proxyBoard);
        ReflectionTestUtils.setField(comment, "id", commentId);

        return Optional.of(comment);
    }

    @DisplayName("다른 유저가 댓글 수정을 시도하면 오류")
    @Test
    public void IllegalUpdateTest() {
        //mocking
        Integer commentId = 1;
        given(commentRepository.findById(commentId))
                .willReturn(expectedCommentAfterFind(commentId, proxyWriter, proxyBoard));

        //given
        CommentUpdateDto param = new CommentUpdateDto(1, "내용 수정 좀 할게요.");

        //when
        assertThrows(RuntimeException.class,
                () -> commentService.update(param, new MemberId(99999999)));
    }

    @DisplayName("댓글 리스트를 찾는 메소드를 호출한다.")
    @Test
    public void getComments() {
        //when
        commentService.getComments(proxyBoard.getId());

        //then
        verify(commentRepository, times(1))
                .findAllByParentBoardIdOrderByCreated(proxyBoard.getId());
    }

    @DisplayName("댓글을 성공적으로 삭제한다.")
    @Test
    public void deleteComment() {
        //given

        given(commentRepository.findById(anyInt()))
                .willReturn(expectedCommentAfterFind(1, proxyWriter, proxyBoard));
        doNothing().when(commentRepository).deleteById(isA(Integer.class));

        //when
        commentService.delete(1, proxyWriter.getId());

        //then
        verify(commentRepository, times(1))
                .deleteById(1);
    }

    @DisplayName("다른 사람이 삭제 시도하면 오류")
    @Test
    public void cannotDeleteComment() {
        //given
        MemberId otherMember = new MemberId(11111111);
        given(commentRepository.findById(anyInt()))
                .willReturn(expectedCommentAfterFind(1, proxyWriter, proxyBoard));

        //when
        assertThrows(RuntimeException.class,
                () -> commentService.delete(1, otherMember));
    }
}
