package com.inhabas.api.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.domain.team.SuchTeamNotFoundException;
import com.inhabas.api.domain.team.dto.TeamDto;
import com.inhabas.api.domain.team.dto.TeamSaveDto;
import com.inhabas.api.domain.team.usecase.TeamService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@NoSecureWebMvcTest(TeamController.class)
public class TeamControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeamService teamService;


    @DisplayName("팀 생성 요청")
    @Test
    public void createTeamTest() throws Exception {
        doNothing().when(teamService).create(any());

        //when
        mvc.perform(post("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TeamSaveDto( "마케팅부서"))))
                .andExpect(status().isNoContent());
    }

    @DisplayName("팀 정보 요청")
    @Test
    public void getTeamInfoTest() throws Exception {
        given(teamService.getTeamInfo(anyInt())).willReturn(new TeamDto(1, "IT 부서"));

        //when
        mvc.perform(get("/team/1"))
                .andExpect(status().isOk());

        then(teamService).should(times(1)).getTeamInfo(anyInt());
    }

    @DisplayName("존재하지 않는 팀 정보 요청")
    @Test
    public void getInvalidTeamInfoTest() throws Exception {
        given(teamService.getTeamInfo(anyInt())).willThrow(SuchTeamNotFoundException.class);

        //when
        mvc.perform(get("/team/1"))
                .andExpect(status().isBadRequest());

        then(teamService).should(times(1)).getTeamInfo(anyInt());
    }

    @DisplayName("팀 전제 정보 요청")
    @Test
    public void getAllTeamInfoTest() throws Exception {
        given(teamService.getAllTeamInfo()).willReturn(List.of());

        //when
        mvc.perform(get("/teams"))
                .andExpect(status().isOk());

        then(teamService).should(times(1)).getAllTeamInfo();
    }

    @DisplayName("팀 삭제 요청")
    @Test
    public void deleteTeamTest() throws Exception {

        doNothing().when(teamService).delete(anyInt());

        //when
        mvc.perform(delete("/team/1"))
                .andExpect(status().isNoContent());

        then(teamService).should(times(1)).delete(anyInt());
    }

    @DisplayName("존재하지 않는 팀 삭제 요청")
    @Test
    public void deleteInvalidTeamTest() throws Exception {

        doThrow(SuchTeamNotFoundException.class).when(teamService).delete(anyInt());

        //when
        mvc.perform(delete("/team/1"))
                .andExpect(status().isBadRequest());

        then(teamService).should(times(1)).delete(anyInt());
    }

    @DisplayName("팀 이름 수정 요청")
    @Test
    public void updateTeamInfoTest() throws Exception {
        doNothing().when(teamService).update(any());

        //when
        mvc.perform(put("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TeamDto(1, "마케팅부서"))))
                .andExpect(status().isNoContent());

        then(teamService).should(times(1)).update(any());
    }

    @DisplayName("존재하지 않는 팀 이름 수정 요청")
    @Test
    public void updateInvalidTeamInfoTest() throws Exception {

        doThrow(SuchTeamNotFoundException.class).when(teamService).update(any());

        //when
        mvc.perform(put("/team")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TeamDto(1, "마케팅부서"))))
                .andExpect(status().isBadRequest());

        then(teamService).should(times(1)).update(any());
    }
}
