package com.inhabas.api.domain.board;

import com.inhabas.api.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NormalBoardRepositoryCustom {

    Page<BoardDto> findAllByMenuId(Integer menuId, Pageable pageable);
    Optional<BoardDto> findDtoById(Integer id);
}
