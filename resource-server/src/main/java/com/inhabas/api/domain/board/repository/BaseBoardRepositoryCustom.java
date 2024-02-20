package com.inhabas.api.domain.board.repository;

import java.util.List;

import com.inhabas.api.domain.board.dto.BoardCountDto;

public interface BaseBoardRepositoryCustom {

  List<BoardCountDto> countRowsGroupByMenuName(Integer menuGroupId);
}
