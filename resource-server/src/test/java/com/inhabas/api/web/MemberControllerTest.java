package com.inhabas.api.web;

import com.inhabas.api.domain.member.dto.ContactDto;
import com.inhabas.api.domain.member.domain.MemberService;
import com.inhabas.api.domain.team.usecase.MemberTeamService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoSecureWebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberTeamService memberTeamService;

    @DisplayName("회원을 한 팀에 추가시킨다.")
    @Test
    public void addMemberToTeamTest() throws Exception {
        doNothing().when(memberTeamService).addMemberToTeam(any(), anyInt());

        mvc.perform(post("/member/team").with(csrf())
                        .param("memberId", "12171652")
                        .param("teamId", "1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("팀에서 회원을 방출시킨다.")
    @Test
    public void expelMemberFromTeamTest() throws Exception {
        doNothing().when(memberTeamService).deleteMemberFromTeam(any(), anyInt());

        mvc.perform(delete("/member/team").with(csrf())
                        .param("memberId", "12171652")
                        .param("teamId", "1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("회장 연락처 정보를 불러온다")
    @Test
    public void getChiefContactInfoTest() throws Exception {
        given(memberService.getChiefContact())
                .willReturn(new ContactDto("강지훈", "010-0000-0000","my@email.com"));

        mvc.perform(get("/member/chief"))
                .andExpect(status().isOk());
    }
}
