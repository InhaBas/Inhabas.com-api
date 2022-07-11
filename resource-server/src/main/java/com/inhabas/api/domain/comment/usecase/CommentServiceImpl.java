package com.inhabas.api.domain.comment.usecase;

import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.repository.NormalBoardRepository;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.repository.CommentRepository;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final NormalBoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentDetailDto> getComments(Integer boardId) {
        return commentRepository.findAllByParentBoardIdOrderByCreated(boardId);
    }

    @Transactional
    public Integer create(CommentSaveDto commentSaveDto) {
        Member writer = getWriterFrom(commentSaveDto);
        NormalBoard board = getBoardFrom(commentSaveDto);

        Comment newComment = new Comment(commentSaveDto.getContents())
                .toBoard(board)
                .writtenBy(writer);

        return commentRepository.save(newComment).getId();
    }

    private NormalBoard getBoardFrom(CommentSaveDto commentSaveDto) {
        return boardRepository.getById(commentSaveDto.getBoardId());
    }

    private Member getWriterFrom(CommentSaveDto commentSaveDto) {
        return memberRepository.getById(commentSaveDto.getWriterId());
    }

    @Transactional
    public Integer update(CommentUpdateDto commentUpdateDto) {

        Integer id = commentUpdateDto.getId();
        Comment OldComment = commentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        MemberId updaterId = commentUpdateDto.getWriterId();
        if (OldComment.isWrittenBy(updaterId)) {
            OldComment.setContents(commentUpdateDto.getContents());
            commentRepository.save(OldComment);

            return id;
        }
        else {
            throw new RuntimeException("작성자만 수정 가능합니다.");
        }
    }

    @Transactional
    public void delete(Integer id) {
        // 향후 로그인 유저 받아서 처리해야함.
        commentRepository.deleteById(id);
    }
}