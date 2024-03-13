package com.inhabas.api.domain.project.usecase;

import java.util.List;

import com.inhabas.api.domain.project.domain.ProjectBoardType;
import com.inhabas.api.domain.project.dto.ProjectBoardDetailDto;
import com.inhabas.api.domain.project.dto.ProjectBoardDto;
import com.inhabas.api.domain.project.dto.SaveProjectBoardDto;

public interface ProjectBoardService {

  List<ProjectBoardDto> getPinned(ProjectBoardType projectBoardType);

  List<ProjectBoardDto> getPosts(ProjectBoardType projectBoardType, String search);

  ProjectBoardDetailDto getPost(Long memberId, ProjectBoardType projectBoardType, Long boardId);

  Long write(
      Long memberId, ProjectBoardType projectBoardType, SaveProjectBoardDto saveProjectBoardDto);

  void update(
      Long boardId, ProjectBoardType projectBoardType, SaveProjectBoardDto saveProjectBoardDto);

  void delete(Long boardId);
}
