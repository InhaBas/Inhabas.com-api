package com.inhabas.api.domain.normalBoard.repository;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import com.inhabas.api.domain.normalBoard.domain.NormalBoardType;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;

public interface NormalBoardRepositoryCustom {

  List<NormalBoardDto> findAllByTypeAndIsPinned(NormalBoardType boardType);

  List<NormalBoardDto> findAllByTypeAndSearch(NormalBoardType boardType, String search);

  Optional<NormalBoard> findByTypeAndId(NormalBoardType boardType, Long boardId);
}
