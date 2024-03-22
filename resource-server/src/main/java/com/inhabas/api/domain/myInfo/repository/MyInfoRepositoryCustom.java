package com.inhabas.api.domain.myInfo.repository;

import java.util.List;

import com.inhabas.api.domain.myInfo.dto.MyBoardsDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;

public interface MyInfoRepositoryCustom {

  List<MyBoardsDto> findAllBoardsByMemberId(Long memberId);

  List<MyCommentsDto> findAllCommentsByMemberId(Long memberId);

  List<MyBudgetSupportApplicationDto> findAllBudgetSupportApplicationsByMemberId(Long memberId);
}
