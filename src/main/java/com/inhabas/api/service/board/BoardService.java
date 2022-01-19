package com.inhabas.api.service.board;


import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    BoardDto write(SaveBoardDto saveBoardDto);

    BoardDto update(UpdateBoardDto updateBoardDto);

    void delete(Integer id);

    Optional<BoardDto> getBoard(Integer categoryId, Integer boardId);

    Page<BoardDto> getBoardList(Pageable pageable, Integer categoryId);
}
