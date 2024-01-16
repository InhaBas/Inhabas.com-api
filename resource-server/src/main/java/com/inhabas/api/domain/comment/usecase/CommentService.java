package com.inhabas.api.domain.comment.usecase;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    List<CommentDetailDto> getComments(Long boardId);

    Long create(CommentSaveDto commentSaveDto, Long memberId);

    Long update(CommentUpdateDto commentUpdateDto, Long memberId);

    void delete(Long boardId, Long memberId);
}
