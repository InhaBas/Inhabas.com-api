package com.inhabas.api.domain.contest.usecase;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.contest.dto.DetailContestBoardDto;
import com.inhabas.api.domain.contest.dto.ListContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.dto.UpdateContestBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContestBoardService {
    Integer write(MemberId memberId, SaveContestBoardDto dto);

    Integer update(MemberId memberId, UpdateContestBoardDto dto);

    void delete(Integer id);

    DetailContestBoardDto getBoard(Integer id);

    Page<ListContestBoardDto> getBoardList(MenuId menuId, Pageable pageable);

}
