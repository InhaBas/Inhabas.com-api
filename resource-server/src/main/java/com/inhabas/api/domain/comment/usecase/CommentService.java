package com.inhabas.api.domain.comment.usecase;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    List<CommentDetailDto> getComments(Integer boardId);

    Integer create(CommentSaveDto commentSaveDto);

    Integer update(CommentUpdateDto commentUpdateDto);

    void delete(Integer id);
}
