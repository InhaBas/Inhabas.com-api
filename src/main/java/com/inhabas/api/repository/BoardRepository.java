package com.inhabas.api.repository;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.Board;
import com.inhabas.api.dto.BoardDto;

import java.util.List;

public interface BoardRepository {

    public Board save(Board board);

    public Board findById(Long id);

    public List<Board> findAll();

    public List<Board> findByType(Category type);

    public void deleteById(Long id);

    public void update(Long id, BoardDto board);

}
