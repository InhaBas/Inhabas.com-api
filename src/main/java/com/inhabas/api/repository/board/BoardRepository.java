package com.inhabas.api.repository.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    public Board save(Board board);

    public Optional<Board> findById(Integer id);

    public List<Board> findAll();

    public List<Board> findAllByCategory(Category type);

    public void deleteById(Integer id);

    public Board update(Board board);

}
