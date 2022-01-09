package com.inhabas.api.service.board;


import com.inhabas.api.dto.NormalBoardDto;

import java.util.List;

public interface BoardService {
    void write(NormalBoardDto board);

    void modify(NormalBoardDto board);

    void delete(Integer id);

    void getBoard(NormalBoardDto board);

    List<NormalBoardDto> getBoardList(NormalBoardDto board);
}
