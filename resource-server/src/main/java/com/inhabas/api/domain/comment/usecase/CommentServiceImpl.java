package com.inhabas.api.domain.comment.usecase;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final EntityManager em;
  private final CommentRepository commentRepository;

  @Transactional(readOnly = true)
  public List<CommentDetailDto> getComments(Integer menuId, Long boardId) {

    return commentRepository.findAllByParentBoardIdOrderByCreated(boardId);
  }

  @Transactional
  public Long create(CommentSaveDto commentSaveDto, Integer menuId, Long boardId, Long memberId) {

    BaseBoard parentBoard = em.getReference(BaseBoard.class, boardId);
    Member writer = em.getReference(Member.class, memberId);
    Comment newComment = new Comment(commentSaveDto.getContent(), writer, parentBoard);

    if (commentSaveDto.isNotRootComment()) {
      Comment parentComment = em.getReference(Comment.class, commentSaveDto.getParentCommentId());
      newComment.replyTo(parentComment);
    }

    return commentRepository.save(newComment).getId();
  }

  @Transactional
  public Long update(Long commentId, CommentUpdateDto commentUpdateDto) {

    Comment oldComment = commentRepository.findById(commentId).orElseThrow(NotFoundException::new);

    return oldComment.update(commentUpdateDto.getContent());
  }

  @Transactional
  public void delete(Long commentId) {

    Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundException::new);
    comment.updateIsDeleted(true);
  }
}
