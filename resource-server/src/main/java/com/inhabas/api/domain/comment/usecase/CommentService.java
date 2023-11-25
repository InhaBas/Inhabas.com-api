package com.inhabas.api.domain.comment.usecase;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;

import java.util.List;

public interface CommentService {

    List<CommentDetailDto> getComments(Integer boardId);

    Integer create(CommentSaveDto commentSaveDto, StudentId writerId);

    Integer update(CommentUpdateDto commentUpdateDto, StudentId writerId);

    void delete(Integer boardId, StudentId writerId);
}
