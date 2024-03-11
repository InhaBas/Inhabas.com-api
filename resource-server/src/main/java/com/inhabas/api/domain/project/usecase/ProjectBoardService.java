package com.inhabas.api.domain.project.usecase;

import java.util.List;

import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;
import com.inhabas.api.domain.project.ProjectBoardType;

public interface ProjectBoardService {

  List<NormalBoardDto> getPinned(ProjectBoardType projectBoardType);

  List<NormalBoardDto> getPosts(ProjectBoardType projectBoardType, String search);

  NormalBoardDetailDto getPost(Long memberId, ProjectBoardType projectBoardType, Long boardId);

  Long write(
      Long memberId, ProjectBoardType projectBoardType, SaveNormalBoardDto saveNormalBoardDto);

  void update(
      Long boardId, ProjectBoardType projectBoardType, SaveNormalBoardDto saveNormalBoardDto);

  void delete(Long boardId);
}
