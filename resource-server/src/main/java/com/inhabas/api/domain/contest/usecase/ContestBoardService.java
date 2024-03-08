package com.inhabas.api.domain.contest.usecase;

import java.util.List;

import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.contest.domain.valueObject.OrderBy;
import com.inhabas.api.domain.contest.dto.ContestBoardDetailDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;

public interface ContestBoardService {

  List<ContestBoardDto> getContestBoards(
      ContestType contestType, Long contestFieldId, String search, OrderBy orderBy);

  Long writeContestBoard(
      Long memberId, ContestType contestType, SaveContestBoardDto saveContestBoardDto);

  // 공모전 게시판 단일조회
  ContestBoardDetailDto getContestBoard(ContestType contestType, Long boardId);

  void updateContestBoard(
      Long boardId, ContestType contestType, SaveContestBoardDto saveContestBoardDto);

  void deleteContestBoard(Long boardId);
}
