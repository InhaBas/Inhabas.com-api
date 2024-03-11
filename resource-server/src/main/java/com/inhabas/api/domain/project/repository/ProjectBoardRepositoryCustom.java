package com.inhabas.api.domain.project.repository;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.project.ProjectBoardType;

public interface ProjectBoardRepositoryCustom {

  List<NormalBoardDto> findAllByTypeAndIsPinned(ProjectBoardType projectBoardType);

  List<NormalBoardDto> findAllByMemberIdAndTypeAndSearch(
      Long memberId, ProjectBoardType projectBoardType, String search);

  List<NormalBoardDto> findAllByTypeAndSearch(ProjectBoardType projectBoardType, String search);

  Optional<NormalBoard> findByMemberIdAndTypeAndId(
      Long memberId, ProjectBoardType projectBoardType, Long boardId);

  Optional<NormalBoard> findByTypeAndId(ProjectBoardType projectBoardType, Long boardId);
}
