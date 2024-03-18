package com.inhabas.api.domain.myInfo.usecase;

import java.util.List;

import com.inhabas.api.domain.myInfo.dto.MyPostsDto;

public interface MyInfoService {
  List<MyPostsDto> getNormalBoards();

  List<MyPostsDto> getProjectBoards();

  List<MyPostsDto> getContestBoards();

  // List<MyCommentsDto> getComments(Long memberId, Long menuId, Long boardId);

  // List<MyBudgetSupportApplicationDto> getBudgetSupportApplications(Long memberId, Long menuId,
  // Long boardId);
}
