package com.inhabas.api.domain.contest.repository;

import java.util.List;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Name;
import com.inhabas.api.domain.contest.domain.ContestBoard;

public interface ContestBoardRepositoryCustom {

  //    Optional<DetailContestBoardDto> findDtoById(Integer id);
  //
  //    Page<ListContestBoardDto> findAllByMenuId(MenuId menuId, Pageable pageable);

  List<ContestBoard> findAllByWriterNameLike(Name writerName, String name);

  List<ContestBoard> findAllByTitleLike(String title);

  List<ContestBoard> findAllByContentLike(String content);
}
