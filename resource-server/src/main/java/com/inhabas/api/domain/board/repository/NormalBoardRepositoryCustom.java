package com.inhabas.api.domain.board.repository;

import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.domain.NormalBoardType;
import com.inhabas.api.domain.board.dto.NormalBoardDto;

import java.util.List;
import java.util.Optional;

public interface NormalBoardRepositoryCustom {

  List<NormalBoardDto> findAllByMemberIdAndTypeAndSearch(Long memberId, NormalBoardType boardType, String search);
  List<NormalBoardDto> findAllByTypeAndSearch(NormalBoardType boardType, String search);
  Optional<NormalBoard> findByMemberIdAndTypeAndId(Long memberId, NormalBoardType boardType, Long boardId);
  Optional<NormalBoard> findByTypeAndId(NormalBoardType boardType, Long boardId);
}
