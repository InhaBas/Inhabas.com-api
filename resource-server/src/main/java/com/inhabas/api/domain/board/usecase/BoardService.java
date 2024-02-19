package com.inhabas.api.domain.board.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inhabas.api.domain.board.dto.BoardDto;
import com.inhabas.api.domain.board.dto.SaveBoardDto;
import com.inhabas.api.domain.board.dto.UpdateBoardDto;

public interface BoardService {
  Long write(Long memberId, SaveBoardDto saveBoardDto);

  Long update(Long memberId, UpdateBoardDto updateBoardDto);

  void delete(Long memberId, Long boardId);

  BoardDto getBoard(Long boardId);

  Page<BoardDto> getBoardList(Integer menuId, Pageable pageable);
}
