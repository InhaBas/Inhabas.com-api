package com.inhabas.api.domain.comment.usecase;

import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.repository.CommentRepository;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final EntityManager em;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentDetailDto> getComments(Integer boardId) {
        return commentRepository.findAllByParentBoardIdOrderByCreated(boardId);
    }


    @Transactional
    public Integer create(CommentSaveDto commentSaveDto, MemberId writerId) {

        NormalBoard parentBoard = em.getReference(NormalBoard.class, commentSaveDto.getBoardId());
        Member writer = em.getReference(Member.class, writerId);
        Comment newComment= new Comment(commentSaveDto.getContents(), writer, parentBoard);

        if (commentSaveDto.isNotRootComment()) {
            Comment parentComment = em.getReference(Comment.class, commentSaveDto.getParentCommentId());
            newComment.replyTo(parentComment);
        }

        return commentRepository.save(newComment).getId();
    }


    @Transactional
    public Integer update(CommentUpdateDto commentUpdateDto, MemberId memberId) {

        Integer id = commentUpdateDto.getCommentId();
        Comment OldComment = commentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return OldComment.update(commentUpdateDto.getContents(), memberId);
    }

    @Transactional
    public void delete(Integer id, MemberId memberId) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (comment.isWrittenBy(memberId))
            commentRepository.deleteById(id);
        else
            throw new RuntimeException("다른 사람이 쓴 댓글은 수정할 수 없습니다.");

    }
}
