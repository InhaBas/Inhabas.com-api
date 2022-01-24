package com.inhabas.api.domain.comment;

import com.inhabas.api.dto.CommentDetailDto;

import java.util.List;

public interface CustomCommentRepository {

    List<CommentDetailDto> findAllByParentBoardIdOrderByCreated(Integer boardId);
}
