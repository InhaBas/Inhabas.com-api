package com.inhabas.api.domain.comment.usecase;

import java.util.List;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;

public interface CommentService {

  List<CommentDetailDto> getComments(Integer menuId, Long boardId);

  Long create(CommentSaveDto commentSaveDto, Integer menuId, Long boardId, Long memberId);

  Long update(Long commentId, CommentUpdateDto commentUpdateDto);

  void delete(Long boardId);
}
