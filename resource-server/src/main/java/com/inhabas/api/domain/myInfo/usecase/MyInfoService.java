package com.inhabas.api.domain.myInfo.usecase;

import java.util.List;

import com.inhabas.api.domain.myInfo.dto.MyBoardsDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;

public interface MyInfoService {
  List<MyBoardsDto> getMyBoards();

  List<MyCommentsDto> getMyComments();

  List<MyBudgetSupportApplicationDto> getMyBudgetSupportApplications();
}
