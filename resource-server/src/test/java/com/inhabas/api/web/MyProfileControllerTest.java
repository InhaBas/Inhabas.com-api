package com.inhabas.api.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.dto.MyProfileDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileDetailDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileIntroDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileNameDto;
import com.inhabas.api.auth.domain.oauth2.member.service.MemberService;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoSecureWebMvcTest(MyProfileController.class)
class MyProfileControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;


    private String jsonOf(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    public void getMyProfileTest() throws Exception {
        //given
        Member member = MemberTest.basicMember1();
        MyProfileDto myProfileDto = MyProfileDto.builder()
                .name(member.getName())
                .studentId(member.getStudentId())
                .major(member.getSchoolInformation().getMajor())
                .grade(member.getSchoolInformation().getGrade())
                .email(member.getEmail())
                .phoneNumber(member.getPhone())
                .role(member.getRole())
                .type(member.getSchoolInformation().getMemberType())
                .introduce(member.getIbasInformation().getIntroduce())
                .build();

        //when
        given(memberService.getMyProfile(any())).willReturn(myProfileDto);

        //then
        String response = mvc.perform(get("/myInfo"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertThat(response).isEqualTo(jsonOf(myProfileDto));

    }

    @DisplayName("내 [학과, 학년, 전화번호] 수정")
    @Test
    public void updateMyProfileDetailTest() throws Exception {
        //given
        Member member = MemberTest.basicMember1();
        ProfileDetailDto profileDetailDto = ProfileDetailDto.builder()
                .major(member.getSchoolInformation().getMajor())
                .grade(member.getSchoolInformation().getGrade())
                .phoneNumber(member.getPhone())
                .build();

        //when
        willDoNothing().given(memberService).updateMyProfileDetail(any(), any());

        //then
        mvc.perform(put("/myInfo/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(profileDetailDto)))
                .andExpect(status().isNoContent());

    }

    @DisplayName("내 프로필 자기소개 수정")
    @Test
    public void updateMyProfileIntroTest() throws Exception {
        //given
        Member member = MemberTest.basicMember1();
        ProfileIntroDto profileIntroDto = ProfileIntroDto.builder()
                .introduce(member.getIbasInformation().getIntroduce())
                .isHOF(member.getIbasInformation().getIsHOF())
                .build();

        //when
        willDoNothing().given(memberService).updateMyProfileIntro(any(), any());

        //then
        mvc.perform(put("/myInfo/intro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(profileIntroDto)))
                .andExpect(status().isNoContent());

    }

    @DisplayName("내 프로필 사진 수정")
    @Test
    @Disabled
    public void updateMyProfileImage() throws Exception {
        // 첨부파일 문제로 보류
    }

    @DisplayName("내 정보 이름 수정")
    @Test
    public void requestMyProfileNameTest() throws Exception {
        //given
        ProfileNameDto profileNameDto = ProfileNameDto.builder()
                .name("새이름")
                .build();

        //when
        willDoNothing().given(memberService).requestMyProfileName(any(), any());

        //then
        mvc.perform(put("/myInfo/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(profileNameDto)))
                .andExpect(status().isNoContent());

    }

    @DisplayName("내 정보 이름 수정에서 이름은 NotBlank 를 보장한다.")
    @Test
    public void wrongRequestMyProfileNameTest() throws Exception {
        //given
        ProfileNameDto nullNameDto = ProfileNameDto.builder()
                .name(null)
                .build();
        ProfileNameDto emptyNameDto = ProfileNameDto.builder()
                .name(" ")
                .build();


        //when
        willDoNothing().given(memberService).requestMyProfileName(any(), any());

        //then
        mvc.perform(put("/myInfo/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(nullNameDto)))
                .andExpect(status().isBadRequest());

        mvc.perform(put("/myInfo/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(emptyNameDto)))
                .andExpect(status().isBadRequest());

    }

}