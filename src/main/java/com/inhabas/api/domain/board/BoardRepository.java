package com.inhabas.api.domain.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<NormalBoard, Integer> {

    Page<NormalBoard> findAllByCategory(Category category, Pageable pageable);

    List<NormalBoard> findAllByCategory(Category category);
}

