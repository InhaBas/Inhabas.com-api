package com.inhabas.api.domain.board.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inhabas.api.domain.board.dto.BoardDto;

public interface NormalBoardRepositoryCustom {

  Page<BoardDto> findAllByMenuId(Integer menuId, Pageable pageable);

  Optional<BoardDto> findDtoById(Long id);
}
