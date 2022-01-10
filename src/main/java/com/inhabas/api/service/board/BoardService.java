package com.inhabas.api.service.board;


import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.NormalBoardDto;

import java.util.List;
import java.util.Optional;

public interface BoardService {
    Integer write(NormalBoardDto board);

    Integer modify(NormalBoardDto board);

    void delete(Integer id);

    Optional<NormalBoard> getBoard(Integer id);

    List<NormalBoard> getBoardList(Category category);
}
