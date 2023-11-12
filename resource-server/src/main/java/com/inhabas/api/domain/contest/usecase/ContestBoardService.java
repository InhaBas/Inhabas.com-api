package com.inhabas.api.domain.contest.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.contest.dto.DetailContestBoardDto;
import com.inhabas.api.domain.contest.dto.ListContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.dto.UpdateContestBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContestBoardService {
    Integer write(StudentId studentId, SaveContestBoardDto dto);

    Integer update(StudentId studentId, UpdateContestBoardDto dto);

    void delete(StudentId studentId, Integer id);

    DetailContestBoardDto getBoard(Integer id);

    Page<ListContestBoardDto> getBoardList(MenuId menuId, Pageable pageable);

}
