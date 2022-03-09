package com.inhabas.api.service.comment;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.comment.CommentDetailDto;
import com.inhabas.api.dto.comment.CommentSaveDto;
import com.inhabas.api.dto.comment.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    List<CommentDetailDto> getComments(Integer boardId);

    Integer create(CommentSaveDto commentSaveDto);

    Integer update(CommentUpdateDto commentUpdateDto);

    void delete(Integer id);
}
