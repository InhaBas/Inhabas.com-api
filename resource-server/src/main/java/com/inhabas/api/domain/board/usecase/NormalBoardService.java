package com.inhabas.api.domain.board.usecase;

import com.inhabas.api.domain.board.domain.NormalBoardType;

import com.inhabas.api.domain.board.dto.BoardCountDto;
import com.inhabas.api.domain.board.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.board.dto.NormalBoardDto;
import com.inhabas.api.domain.board.dto.SaveNormalBoardDto;

import java.util.List;

public interface NormalBoardService {

  List<BoardCountDto> getPostCount();

  List<NormalBoardDto> getPosts(Long memberId, NormalBoardType boardType, String search);

  NormalBoardDetailDto getPost(Long memberId, NormalBoardType boardType, Long boardId);

  Long write(Long memberId, NormalBoardType normalBoardType, SaveNormalBoardDto saveNormalBoardDto);

  void update(Long boardId, NormalBoardType normalBoardType, SaveNormalBoardDto saveNormalBoardDto);

  void delete(Long boardId);
}
