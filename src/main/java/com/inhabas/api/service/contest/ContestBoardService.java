package com.inhabas.api.service.contest;

import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.inhabas.api.dto.contest.SaveContestBoardDto;
import com.inhabas.api.dto.contest.UpdateContestBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContestBoardService {
    Integer write(Integer userId, SaveContestBoardDto dto);

    Integer update(Integer userId, UpdateContestBoardDto dto);

    void delete(Integer id);

    DetailContestBoardDto getBoard(Integer id);

    Page<ListContestBoardDto> getBoardList(Integer menuId, Pageable pageable);

}
