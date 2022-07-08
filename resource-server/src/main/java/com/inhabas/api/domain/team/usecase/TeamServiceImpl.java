package com.inhabas.api.domain.team.usecase;

import com.inhabas.api.domain.team.domain.Team;
import com.inhabas.api.domain.team.repository.TeamRepository;
import com.inhabas.api.domain.team.dto.TeamDto;
import com.inhabas.api.domain.team.dto.TeamSaveDto;
import com.inhabas.api.domain.team.SuchTeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    @Transactional(readOnly = true)
    public void create(TeamSaveDto teamSaveDto) {
        teamRepository.save(new Team(teamSaveDto.getTeamName()));
    }

    @Override
    @Transactional
    public void delete(Integer teamId) {
        try {
            teamRepository.deleteById(teamId);
        } catch (IllegalStateException e) {
            throw new SuchTeamNotFoundException();
        }
    }

    @Override
    @Transactional
    public void update(TeamDto teamDto) {
        Team team = teamRepository.findById(teamDto.getId())
                .orElseThrow(SuchTeamNotFoundException::new);
        teamRepository.save(team);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamDto getTeamInfo(Integer teamId) {
        return new TeamDto(teamRepository.findById(teamId)
                .orElseThrow(SuchTeamNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamDto> getAllTeamInfo() {
        return teamRepository.findAll()
                .stream().map(TeamDto::new)
                .collect(Collectors.toUnmodifiableList());
    }

}
