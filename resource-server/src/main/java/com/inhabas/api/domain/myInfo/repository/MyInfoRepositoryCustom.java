package com.inhabas.api.domain.myInfo.repository;

import java.util.List;

import com.inhabas.api.domain.myInfo.dto.MyPostsDto;

public interface MyInfoRepositoryCustom {

  List<MyPostsDto> findAllNormalBoardsByMemberId(Long memberId);

  List<MyPostsDto> findAllProjectBoardsByMemberId(Long memberId);

  List<MyPostsDto> findAllContestBoardsByMemberId(Long memberId);
}
