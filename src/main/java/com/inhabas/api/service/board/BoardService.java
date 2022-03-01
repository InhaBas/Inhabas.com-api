package com.inhabas.api.service.board;

import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Integer write(Integer memberId, SaveBoardDto saveBoardDto);

    Integer update(Integer memberId, UpdateBoardDto updateBoardDto);

    void delete(Integer id);

    BoardDto getBoard(Integer boardId);

    Page<BoardDto> getBoardList(Integer menuId, Pageable pageable);

}
