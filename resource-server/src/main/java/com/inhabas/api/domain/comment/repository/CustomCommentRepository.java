package com.inhabas.api.domain.comment.repository;

import java.util.List;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;

public interface CustomCommentRepository {

  List<CommentDetailDto> findAllByParentBoardIdOrderByCreated(Long boardId);
}
