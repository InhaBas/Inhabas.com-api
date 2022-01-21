package com.inhabas.api.domain.board;

import com.inhabas.api.dto.board.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NormalBoardRepository {

    Page<BoardDto> findAllByCategoryId(Integer categoryId, Pageable pageable);

    BoardDto save(NormalBoard entity);

    Optional<BoardDto> findById(Integer integer);

    void deleteById(Integer integer);

}
