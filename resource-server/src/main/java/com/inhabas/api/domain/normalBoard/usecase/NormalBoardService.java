package com.inhabas.api.domain.normalBoard.usecase;

import com.inhabas.api.domain.normalBoard.domain.NormalBoardType;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;

import java.util.List;

public interface NormalBoardService {

  List<NormalBoardDto> getPinned(NormalBoardType boardType);

  List<NormalBoardDto> getPosts(NormalBoardType boardType, String search);

  NormalBoardDetailDto getPost(Long memberId, NormalBoardType boardType, Long boardId);

  Long write(Long memberId, NormalBoardType normalBoardType, SaveNormalBoardDto saveNormalBoardDto);

  void update(Long boardId, NormalBoardType normalBoardType, SaveNormalBoardDto saveNormalBoardDto);

  void delete(Long boardId);
}
