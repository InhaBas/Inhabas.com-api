package com.inhabas.api.domain.myInfo.usecase;

import java.util.List;

import com.inhabas.api.domain.myInfo.dto.MyBoardDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentDto;

public interface MyInfoService {
  List<MyBoardDto> getMyBoards(Long memberId);

  List<MyCommentDto> getMyComments(Long memberId);

  List<MyBudgetSupportApplicationDto> getMyBudgetSupportApplications(Long memberId);
}
