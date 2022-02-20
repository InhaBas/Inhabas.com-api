package com.inhabas.api.domain.contest;

import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContestBoardRepositoryCustom {

    Optional<DetailContestBoardDto> findDtoById(Integer menuId, Integer id);

    Page<ListContestBoardDto> findAllByMenuId(Integer menuId, Pageable pageable);

}
