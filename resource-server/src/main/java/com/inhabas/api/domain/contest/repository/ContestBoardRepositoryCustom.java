package com.inhabas.api.domain.contest.repository;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;

public interface ContestBoardRepositoryCustom {

  List<ContestBoardDto> findAllByTypeAndFieldAndSearch(
      ContestType contestType, Long contestFieldId, String search, String sortBy);

  public Optional<ContestBoard> findByTypeAndId(ContestType contestType, Long boardId);
}
