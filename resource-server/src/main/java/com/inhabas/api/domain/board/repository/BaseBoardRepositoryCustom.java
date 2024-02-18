package com.inhabas.api.domain.board.repository;

import com.inhabas.api.domain.board.dto.BoardCountDto;

import java.util.List;

public interface BaseBoardRepositoryCustom {

    List<BoardCountDto> countRowsGroupByMenuName(Integer menuGroupId);
}
