package com.inhabas.api.domain.board.repository;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.board.dto.BoardDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NormalBoardRepositoryCustom {

    Page<BoardDto> findAllByMenuId(MenuId menuId, Pageable pageable);
    Optional<BoardDto> findDtoById(Integer id);
}
