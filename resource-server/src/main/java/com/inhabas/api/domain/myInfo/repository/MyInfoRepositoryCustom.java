package com.inhabas.api.domain.myInfo.repository;

import java.util.List;

import com.inhabas.api.domain.myInfo.dto.MyBoardDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentDto;

public interface MyInfoRepositoryCustom {

  List<MyBoardDto> findAllBoardsByMemberId(Long memberId);

  List<MyCommentDto> findAllCommentsByMemberId(Long memberId);

  List<MyBudgetSupportApplicationDto> findAllBudgetSupportApplicationsByMemberId(Long memberId);
}
