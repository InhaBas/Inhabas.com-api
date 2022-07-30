package com.inhabas.api.domain.comment.usecase;

import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import java.util.List;

public interface CommentService {

    List<CommentDetailDto> getComments(Integer boardId);

    Integer create(CommentSaveDto commentSaveDto, MemberId writerId);

    Integer update(CommentUpdateDto commentUpdateDto, MemberId writerId);

    void delete(Integer boardId, MemberId writerId);
}
