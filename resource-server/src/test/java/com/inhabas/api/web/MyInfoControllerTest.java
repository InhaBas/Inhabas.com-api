package com.inhabas.api.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.*;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.member.usecase.MemberProfileService;
import com.inhabas.api.domain.myInfo.dto.MyBoardDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentDto;
import com.inhabas.api.domain.myInfo.usecase.MyInfoService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(MyInfoController.class)
class MyInfoControllerTest {

  @Autowired private MockMvc mvc;
  @MockBean private MemberProfileService memberProfileService;
  @MockBean private MyInfoService myInfoService;
  @Autowired private ObjectMapper objectMapper;

  private String jsonOf(Object o) throws JsonProcessingException {
    return objectMapper.writeValueAsString(o);
  }

  @DisplayName("내 정보를 조회한다.")
  @Test
  public void getMyProfileTest() throws Exception {
    // given
    Member member = MemberTest.basicMember1();
    MyProfileDto myProfileDto =
        MyProfileDto.builder()
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

    // when
    given(memberProfileService.getMyProfile(any())).willReturn(myProfileDto);

    // then
    String response =
        mvc.perform(get("/myInfo"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
    assertThat(response).isEqualTo(jsonOf(myProfileDto));
  }

  @DisplayName("내 [학과, 학년, 전화번호] 수정")
  @Test
  public void updateMyProfileDetailTest() throws Exception {
    // given
    Member member = MemberTest.basicMember1();
    ProfileDetailDto profileDetailDto =
        ProfileDetailDto.builder()
            .major(member.getSchoolInformation().getMajor())
            .grade(member.getSchoolInformation().getGrade())
            .phoneNumber(member.getPhone())
            .build();

    // when
    willDoNothing().given(memberProfileService).updateMyProfileDetail(any(), any());

    // then
    mvc.perform(
            put("/myInfo/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(profileDetailDto)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("내 프로필 자기소개 수정")
  @Test
  public void updateMyProfileIntroTest() throws Exception {
    // given
    Member member = MemberTest.basicMember1();
    ProfileIntroDto profileIntroDto =
        ProfileIntroDto.builder()
            .introduce(member.getIbasInformation().getIntroduce())
            .isHOF(member.getIbasInformation().getIsHOF())
            .build();

    // when
    willDoNothing().given(memberProfileService).updateMyProfileIntro(any(), any());

    // then
    mvc.perform(
            put("/myInfo/intro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(profileIntroDto)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("내 프로필 사진 수정")
  @Test
  public void updateMyProfileImage() throws Exception {
    // given
    MockMultipartFile file =
        new MockMultipartFile("picture", "test.jpeg", "image/jpeg", "<<jpeg data>>".getBytes());

    willDoNothing().given(memberProfileService).updateMyProfileImage(any(), any());

    // when then
    mvc.perform(multipart("/myInfo/picture").file(file)).andExpect(status().isNoContent());
  }

  @DisplayName("내 정보 이름 수정")
  @Test
  public void requestMyProfileNameTest() throws Exception {
    // given
    ProfileNameDto profileNameDto = ProfileNameDto.builder().name("새이름").build();

    // when
    willDoNothing().given(memberProfileService).requestMyProfileName(any(), any());

    // then
    mvc.perform(
            put("/myInfo/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(profileNameDto)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("내 정보 이름 수정에서 이름은 NotBlank 를 보장한다.")
  @Test
  public void wrongRequestMyProfileNameTest() throws Exception {
    // given
    ProfileNameDto nullNameDto = ProfileNameDto.builder().name(null).build();
    ProfileNameDto emptyNameDto = ProfileNameDto.builder().name(" ").build();

    // when
    willDoNothing().given(memberProfileService).requestMyProfileName(any(), any());

    // then
    mvc.perform(
            put("/myInfo/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(nullNameDto)))
        .andExpect(status().isBadRequest());

    mvc.perform(
            put("/myInfo/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(emptyNameDto)))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("내 정보 이름 수정 요청을 모두 조회한다.")
  @Test
  public void getMyInfoMyRequestsTest() throws Exception {
    // given
    UpdateNameRequestDto dto =
        UpdateNameRequestDto.builder()
            .id(1L)
            .major("컴퓨터공학과")
            .role(Role.BASIC)
            .type(MemberType.GRADUATED)
            .studentId("12121212")
            .beforeName("김이전")
            .afterName("김이후")
            .status(RequestStatus.REJECTED)
            .rejectReason("돌아가")
            .dateRequested(LocalDateTime.now())
            .build();
    given(memberProfileService.getMyInfoMyRequests(any())).willReturn(List.of(dto));

    // when then
    mvc.perform(get("/myInfo/myRequests", 0, 10))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].afterName").value(equalTo("김이후")));
  }

  @DisplayName("내 정보 이름 수정 요청을 모두 조회한다.")
  @Test
  public void getMyInfoRequestsTest() throws Exception {
    // given
    UpdateNameRequestDto dto =
        UpdateNameRequestDto.builder()
            .id(1L)
            .major("컴퓨터공학과")
            .role(Role.BASIC)
            .type(MemberType.GRADUATED)
            .studentId("12121212")
            .beforeName("김이전")
            .afterName("김이후")
            .status(RequestStatus.REJECTED)
            .rejectReason("돌아가")
            .dateRequested(LocalDateTime.now())
            .build();
    given(memberProfileService.getMyInfoRequests()).willReturn(List.of(dto));

    // when then
    mvc.perform(get("/myInfo/requests", 0, 10))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].afterName").value(equalTo("김이후")));
  }

  @DisplayName("내 정보 이름 수정 요청을 처리한다.")
  @Test
  public void handleMyInfoRequestTest() throws Exception {
    // given
    HandleNameRequestDto handleNameRequestDto =
        HandleNameRequestDto.builder().id(1L).status("pass").build();

    willDoNothing().given(memberProfileService).handleMyInfoRequest(any());

    // when then
    mvc.perform(
            put("/myInfo/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(handleNameRequestDto)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("내가 작성한 게시글 목록을 조회 성공 200")
  @Test
  public void getMyBoardsTest() throws Exception {
    // given
    MyBoardDto myBoardDto =
        MyBoardDto.builder()
            .id(1L)
            .menuId(4)
            .menuName("공지사항")
            .title("이건 공지")
            .dateCreated(LocalDateTime.now())
            .build();
    given(myInfoService.getMyBoards()).willReturn(List.of(myBoardDto));

    // when
    String response =
        mvc.perform(get("/myInfo/boards"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(jsonOf(List.of(myBoardDto)));
  }

  @DisplayName("내가 작성한 댓글 목록을 조회 성공 200")
  @Test
  public void getMyCommentsTest() throws Exception {
    // given
    MyCommentDto myCommentDto =
        MyCommentDto.builder()
            .id(1L)
            .menuId(1)
            .menuName("자유게시판")
            .content("댓글댓글")
            .dateCreated(LocalDateTime.now())
            .build();
    given(myInfoService.getMyComments()).willReturn(List.of(myCommentDto));

    // when
    String response =
        mvc.perform(get("/myInfo/comments"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(jsonOf(List.of(myCommentDto)));
  }

  @DisplayName("내가 작성한 예산지원신청 목록을 조회 성공 200.")
  @Test
  public void getMyBudgetSupportApplicationsTest() throws Exception {
    // given
    MyBudgetSupportApplicationDto myBudgetSupportApplicationDto =
        MyBudgetSupportApplicationDto.builder()
            .id(1L)
            .status(RequestStatus.COMPLETED)
            .title("1월 ec2 서버비용")
            .dateCreated(LocalDateTime.now())
            .dateChecked(LocalDateTime.now())
            .dateDeposited(LocalDateTime.now())
            .build();
    given(myInfoService.getMyBudgetSupportApplications())
        .willReturn(List.of(myBudgetSupportApplicationDto));

    // when
    String response =
        mvc.perform(get("/myInfo/supports"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(jsonOf(List.of(myBudgetSupportApplicationDto)));
  }
}
