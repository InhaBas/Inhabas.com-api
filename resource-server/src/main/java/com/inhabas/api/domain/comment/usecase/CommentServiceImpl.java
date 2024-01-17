package com.inhabas.api.domain.comment.usecase;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.usecase.BoardSecurityChecker;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.inhabas.api.domain.board.usecase.BoardSecurityChecker.CREATE_COMMENT;
import static com.inhabas.api.domain.board.usecase.BoardSecurityChecker.READ_COMMENT;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final EntityManager em;
    private final CommentRepository commentRepository;
    private final BoardSecurityChecker boardSecurityChecker;

    @Transactional(readOnly = true)
    public List<CommentDetailDto> getComments(Integer menuId, Long boardId) {

        boardSecurityChecker.checkMenuAccess(menuId, READ_COMMENT);
        return commentRepository.findAllByParentBoardIdOrderByCreated(boardId);
    }


    @Transactional
    public Long create(CommentSaveDto commentSaveDto, Integer menuId, Long boardId, Long memberId) {

        boardSecurityChecker.checkMenuAccess(menuId, CREATE_COMMENT);
        BaseBoard parentBoard = em.getReference(BaseBoard.class, boardId);
        Member writer = em.getReference(Member.class, memberId);
        Comment newComment = new Comment(commentSaveDto.getContents(), writer, parentBoard);

        if (commentSaveDto.isNotRootComment()) {
            Comment parentComment = em.getReference(Comment.class, commentSaveDto.getParentCommentId());
            newComment.replyTo(parentComment);
        }

        return commentRepository.save(newComment).getId();

    }


    @Transactional
    public Long update(CommentUpdateDto commentUpdateDto, Long memberId) {

        Long id = commentUpdateDto.getCommentId();
        Comment oldComment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        return oldComment.update(commentUpdateDto.getContent(), memberId);
    }

    @Transactional
    public void delete(Long id, Long memberId) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (!comment.isWrittenBy(memberId))
            throw new RuntimeException("다른 사람이 쓴 댓글은 수정할 수 없습니다.");

        if (comment.getChildrenComment().isEmpty()) {
            comment.updateIsDeleted(true);
        } else {
            commentRepository.delete(getDeletableAncestorComment(comment));
        }

    }

    private Comment getDeletableAncestorComment(Comment comment) {
        Comment parent = comment.getParentComment();

        if (parent != null && parent.getChildrenComment().size() == 1 && parent.getIsDeleted()) {
            return getDeletableAncestorComment(parent);
        } else
            return comment;

    }


}
