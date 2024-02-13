package com.inhabas.api.domain.contest.repository;

import java.util.List;

import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;

public interface ContestBoardRepositoryCustom {

  List<ContestBoard> findAllByContestBoardAndContestTypeLike(
      ContestType contestType, String search);
}
