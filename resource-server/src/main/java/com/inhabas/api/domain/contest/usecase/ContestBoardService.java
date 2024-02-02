package com.inhabas.api.domain.contest.usecase;

import java.util.List;

import com.inhabas.api.domain.contest.dto.ContestBoardDetailDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;

public interface ContestBoardService {

  List<ContestBoardDto> getContestBoard();

  Long writeContestBoard(Long memberId, SaveContestBoardDto saveContestBoardDto);

  ContestBoardDetailDto getContestBoard(Long boardId);

  void updateContestBoard(Long boardId, SaveContestBoardDto saveContestBoardDto);

  void deleteContestBoard(Long boardId);
}
