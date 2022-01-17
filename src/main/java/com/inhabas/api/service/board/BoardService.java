package com.inhabas.api.service.board;


import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    NormalBoard write(SaveBoardDto saveBoardDto);

    NormalBoard update(UpdateBoardDto updateBoardDto);

    void delete(Integer id);

    Optional<NormalBoard> getBoard(Integer id);

    List<NormalBoard> getBoardList(Category category);
}
