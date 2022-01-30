package com.inhabas.api.domain.board;

import com.inhabas.api.dto.board.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NormalBoardRepositoryCustom {

    Page<BoardDto> findAllByMenuId(Integer menuId, PageRequest pageable);
    Optional<BoardDto> findDtoById(Integer id);
}
