package com.inhabas.api.domain.board.repository;

import java.util.Optional;

import com.inhabas.api.domain.board.dto.NormalBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NormalBoardRepositoryCustom {

  Page<NormalBoardDto> findAllByMenuId(Integer menuId);

  Optional<NormalBoardDto> findDtoById(Long id);
}
