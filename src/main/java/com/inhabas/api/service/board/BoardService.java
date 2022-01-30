package com.inhabas.api.service.board;

import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardService {
    Integer write(SaveBoardDto saveBoardDto);

    Integer update(UpdateBoardDto updateBoardDto);

    void delete(Integer id);

    Optional<BoardDto> getBoard( Integer boardId);

    Page<BoardDto> getBoardList(Integer menuId, PageRequest pageable);

}
