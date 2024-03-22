package com.inhabas.api.domain.normalBoard.usecase;

import java.util.List;

import com.inhabas.api.domain.normalBoard.domain.NormalBoardType;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;

public interface NormalBoardService {

  List<NormalBoardDto> getPinned(NormalBoardType boardType);

  List<NormalBoardDto> getPosts(NormalBoardType boardType, String search);

  NormalBoardDetailDto getPost(Long memberId, NormalBoardType boardType, Long boardId);

  Long write(Long memberId, NormalBoardType normalBoardType, SaveNormalBoardDto saveNormalBoardDto);

  void update(
      Long boardId,
      NormalBoardType boardType,
      SaveNormalBoardDto saveNormalBoardDto,
      Long memberId);

  void delete(Long boardId);
}
