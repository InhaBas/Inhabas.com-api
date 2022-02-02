package com.inhabas.api.service.contestBoard;

import com.inhabas.api.dto.contest.DetailContestDto;
import com.inhabas.api.dto.contest.ListContestDto;
import com.inhabas.api.dto.contest.SaveContestBoardDto;
import com.inhabas.api.dto.contest.UpdateContestBoardDto;

import java.util.Optional;

public interface ContestBoardService {
    Integer write(SaveContestBoardDto dto);

    Integer update(UpdateContestBoardDto dto);

    void delete(Integer id);

    Optional<DetailContestDto> getBoard(Integer id);

}
