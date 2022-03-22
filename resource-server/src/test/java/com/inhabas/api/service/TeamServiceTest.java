package com.inhabas.api.service;

import com.inhabas.api.domain.member.Team;
import com.inhabas.api.domain.member.TeamRepository;
import com.inhabas.api.domain.member.type.wrapper.TeamName;
import com.inhabas.api.dto.member.TeamDto;
import com.inhabas.api.dto.member.TeamSaveDto;
import com.inhabas.api.service.member.SuchTeamNotFoundException;
import com.inhabas.api.service.member.TeamServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @InjectMocks
    private TeamServiceImpl teamService;

    @Mock
    private TeamRepository teamRepository;


    @DisplayName("팀을 신규 생성한다.")
    @Test
    public void createTeamTest() {
        //given
        TeamSaveDto dto = new TeamSaveDto("IT 부서");
        given(teamRepository.save(any(Team.class))).willReturn(new Team("IT 부서"));

        //when
        teamService.create(dto);

        //then
        then(teamRepository).should(times(1)).save(any(Team.class));
    }

    @DisplayName("팀을 삭제한다.")
    @Test
    public void deleteTeamTest() {
        //given
        doNothing().when(teamRepository).deleteById(anyInt());

        //when
        teamService.delete(1);

        //then
        then(teamRepository).should(times(1)).deleteById(anyInt());
    }

    @DisplayName("팀을 수정한다.")
    @Test
    public void updateTeamTest() {
        //given
        Team team = new Team("IT 부서");

        ReflectionTestUtils.setField(team, "id", 1);
        given(teamRepository.findById(anyInt())).willReturn(Optional.of(team));

        ReflectionTestUtils.setField(team, "name", new TeamName("마케팅 부서"));
        given(teamRepository.save(any(Team.class))).willReturn(team);

        //when
        TeamDto dto = new TeamDto(1, "마케팅 부서");
        teamService.update(dto);

        //then
        then(teamRepository).should(times(1)).save(any(Team.class));
    }

    @DisplayName("팀 정보를 불러온다.")
    @Test
    public void getTeamInfo() {
        //given
        given(teamRepository.findById(anyInt())).willReturn(Optional.of(new Team("IT 부서")));

        //when
        teamService.getTeamInfo(1);

        //then
        then(teamRepository).should(times(1)).findById(anyInt());
    }

    @DisplayName("해당 팀 id 에 해당하는 팀이 없다.")
    @Test
    public void teamNotFoundException() {
        //given
        given(teamRepository.findById(anyInt())).willReturn(Optional.empty());

        //when
        assertThrows(SuchTeamNotFoundException.class,
                () -> teamService.getTeamInfo(1));
    }

    @DisplayName("모든 팀 정보를 불러온다.")
    @Test
    public void getAllTeamInfo() {
        //given
        given(teamRepository.findAll()).willReturn(List.of());

        //when
        teamService.getAllTeamInfo();

        //then
        then(teamRepository).should(times(1)).findAll();
    }



}
