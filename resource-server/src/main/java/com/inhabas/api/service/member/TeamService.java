package com.inhabas.api.service.member;

import com.inhabas.api.dto.member.TeamDto;
import com.inhabas.api.dto.member.TeamSaveDto;

import java.util.List;

public interface TeamService {
    void create(TeamSaveDto teamSaveDto);

    /**
     * 부서 삭제 시 기존에 해당 부서에 있던 회원들이 모두 방출됨.
     */
    void delete(Integer teamId);

    void update(TeamDto teamDto);

    TeamDto getTeamInfo(Integer teamId);

    List<TeamDto> getAllTeamInfo();
}
