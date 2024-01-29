package com.inhabas.api.domain.comment.repository;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import java.util.List;

public interface CustomCommentRepository {

  List<CommentDetailDto> findAllByParentBoardIdOrderByCreated(Long boardId);
}
