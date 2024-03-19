package com.inhabas.api.domain.project.repository;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.domain.project.domain.ProjectBoard;
import com.inhabas.api.domain.project.domain.ProjectBoardType;
import com.inhabas.api.domain.project.dto.ProjectBoardDto;

public interface ProjectBoardRepositoryCustom {

  List<ProjectBoardDto> findAllByTypeAndIsPinned(ProjectBoardType projectBoardType);

  List<ProjectBoardDto> findAllByMemberIdAndTypeAndSearch(
      Long memberId, ProjectBoardType projectBoardType, String search);

  List<ProjectBoardDto> findAllByTypeAndSearch(ProjectBoardType projectBoardType, String search);

  Optional<ProjectBoard> findByMemberIdAndTypeAndId(
      Long memberId, ProjectBoardType projectBoardType, Long boardId);

  Optional<ProjectBoard> findByTypeAndId(ProjectBoardType projectBoardType, Long boardId);
}
