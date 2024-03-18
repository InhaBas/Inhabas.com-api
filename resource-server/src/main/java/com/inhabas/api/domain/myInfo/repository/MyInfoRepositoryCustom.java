package com.inhabas.api.domain.myInfo.repository;

import java.util.List;

import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;
import com.inhabas.api.domain.myInfo.dto.MyPostsDto;

public interface MyInfoRepositoryCustom {

  List<MyPostsDto> findAllBoardsByMemberId(Long memberId);

  List<MyCommentsDto> findAllCommentsByMemberId(Long memberId);

  List<MyBudgetSupportApplicationDto> findAllBudgetSupportAllpicationByMemberId(Long memberId);
}
