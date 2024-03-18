package com.inhabas.api.domain.myInfo.usecase;

import java.util.List;

import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;
import com.inhabas.api.domain.myInfo.dto.MyPostsDto;

public interface MyInfoService {
  List<MyPostsDto> getMyBoards();

  List<MyCommentsDto> getMyComments();

  List<MyBudgetSupportApplicationDto> getMyBudgetApplicationSupports();
}
