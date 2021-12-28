package com.inhabas.api.repository.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.Board;
import com.inhabas.api.dto.BoardDto;

import java.util.List;

public interface BoardRepository {

    public Board save(Board board);

    public Board findById(Integer id);

    public List<Board> findAll();

    public List<Board> findAllByCategory(Category type);

    public void deleteById(Integer id);

    public void update(Board board);

}
