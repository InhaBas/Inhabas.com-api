package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.GRADUATED;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.UNDERGRADUATE;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.BASIC;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.*;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.member.usecase.MemberManageService;
import com.inhabas.api.domain.signUp.dto.ApplicationDetailDto;
import com.inhabas.api.domain.signUp.dto.QuestionAnswerDto;
import com.inhabas.api.domain.signUp.usecase.AnswerService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@NoSecureWebMvcTest(MemberController.class)
public class MemberControllerTest {

  @Autowired private MockMvc mvc;
  @MockBean private MemberManageService memberManageService;
  @MockBean private AnswerService answerService;
  @MockBean private MemberRepository memberRepository;
  @Autowired private ObjectMapper objectMapper;

  @DisplayName("(신입)미승인 멤버 정보 목록을 불러온다")
  @Test
  public void getUnapprovedMembers() throws Exception {

    // given
    List<NotApprovedMemberManagementDto> dtoList = new ArrayList<>();
    NotApprovedMemberManagementDto dto1 =
        new NotApprovedMemberManagementDto(
            "홍길동",
            1L,
            "12171707",
            "010-1234-2345",
            "abc1234@gmail.com",
            UNDERGRADUATE,
            1,
            "컴퓨터공학과");
    dtoList.add(dto1);
    given(memberManageService.getNotApprovedMembersBySearchAndRole(any())).willReturn(dtoList);

    // then
    mvc.perform(
            get("/members/unapproved").param("page", "0").param("size", "10").param("search", ""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].name").value(equalTo("홍길동")))
        .andExpect(jsonPath("$.data[0].memberId").value(equalTo(1)))
        .andExpect(jsonPath("$.data[0].studentId").value(equalTo("12171707")))
        .andExpect(jsonPath("$.data[0].phoneNumber").value(equalTo("010-1234-2345")))
        .andExpect(jsonPath("$.data[0].email").value(equalTo("abc1234@gmail.com")))
        .andExpect(jsonPath("$.data[0].major").value(equalTo("컴퓨터공학과")));
  }

  @DisplayName("(신입)미승인 멤버 -> 비활동 멤버로 변경할때 state가 pass, fail이면 204를 반환하고 아니면 400를 반환한다..")
  @ParameterizedTest
  @ValueSource(strings = {"pass", "fail", "hacker"})
  public void passOrFailUnapprovedMembers(String state) throws Exception {
    // given
    List<Long> memberIdList = List.of(1L);
    List<Member> members = List.of(MemberTest.notapprovedMember());

    if (state.equals("pass") || state.equals("fail")) {
      // when
      given(memberRepository.findAllById(memberIdList)).willReturn(members);
      // then
      mvc.perform(
              put("/members/unapproved")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(jsonOf(new UpdateRequestDto(memberIdList, state))))
          .andExpect(status().isNoContent());
    } else {
      // when
      doThrow(new InvalidInputException())
          .when(memberManageService)
          .updateUnapprovedMembers(anyList(), anyString());
      // then
      mvc.perform(
              put("/members/unapproved")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(jsonOf(new UpdateRequestDto(memberIdList, state))))
          .andExpect(status().isBadRequest());
    }
  }

  @DisplayName("특정 신입 멤버 지원서를 조회 성공 200")
  @Test
  public void getUnapprovedMemberApplication() throws Exception {

    // given
    List<QuestionAnswerDto> questionAnswerDtoList =
        List.of(new QuestionAnswerDto(1L, "hello", "bye"));
    ApplicationDetailDto applicationDetailDto =
        new ApplicationDetailDto(
            1L,
            "12171707",
            "조승현",
            1,
            "컴공",
            "123@123",
            "010-1111-2222",
            LocalDateTime.now(),
            questionAnswerDtoList);

    given(answerService.getApplication(any())).willReturn(applicationDetailDto);

    // then
    mvc.perform(get("/members/1/application"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberId").value(equalTo(1)));
  }

  @DisplayName("비활동 이상 졸업자 아닌 멤버 목록 조회 성공 200")
  @Test
  public void getNotGraduatedMembers() throws Exception {

    // given
    List<ApprovedMemberManagementDto> dtoList = new ArrayList<>();
    ApprovedMemberManagementDto dto1 =
        new ApprovedMemberManagementDto(
            "홍길동", 1L, "12171707", "010-1234-2345", UNDERGRADUATE, BASIC, 1, "컴퓨터공학과");
    dtoList.add(dto1);

    given(memberManageService.getApprovedMembersBySearchAndRole(any())).willReturn(dtoList);

    // then
    mvc.perform(get("/members/notGraduated"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].name").value(equalTo("홍길동")))
        .andExpect(jsonPath("$.data[0].memberId").value(equalTo(1)))
        .andExpect(jsonPath("$.data[0].studentId").value(equalTo("12171707")))
        .andExpect(jsonPath("$.data[0].phoneNumber").value(equalTo("010-1234-2345")))
        .andExpect(jsonPath("$.data[0].generation").value(equalTo(1)))
        .andExpect(jsonPath("$.data[0].major").value(equalTo("컴퓨터공학과")));
  }

  @DisplayName("졸업자 멤버 목록 조회 성공 200")
  @Test
  public void getGraduatedMembers() throws Exception {

    // given
    List<ApprovedMemberManagementDto> dtoList = new ArrayList<>();
    ApprovedMemberManagementDto dto1 =
        new ApprovedMemberManagementDto(
            "홍길동", 1L, "12171707", "010-1234-2345", GRADUATED, BASIC, 1, "컴퓨터공학과");
    dtoList.add(dto1);

    given(memberManageService.getGraduatedMembersBySearch(any())).willReturn(dtoList);

    // then
    mvc.perform(get("/members/graduated"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].name").value(equalTo("홍길동")))
        .andExpect(jsonPath("$.data[0].memberId").value(equalTo(1)))
        .andExpect(jsonPath("$.data[0].studentId").value(equalTo("12171707")))
        .andExpect(jsonPath("$.data[0].phoneNumber").value(equalTo("010-1234-2345")))
        .andExpect(jsonPath("$.data[0].generation").value(equalTo(1)))
        .andExpect(jsonPath("$.data[0].major").value(equalTo("컴퓨터공학과")));
  }

  @DisplayName("총무 이상 멤버 목록 조회 성공 200")
  @Test
  public void getExecutiveMembers() throws Exception {

    // given
    List<ExecutiveMemberDto> dtoList = new ArrayList<>();
    ExecutiveMemberDto dto1 =
        new ExecutiveMemberDto("홍길동", 1L, "12171707", BASIC, 1, "컴퓨터공학", null);
    dtoList.add(dto1);

    given(memberManageService.getExecutiveMembers()).willReturn(dtoList);

    // then
    mvc.perform(get("/members/executive"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].name").value(equalTo("홍길동")))
        .andExpect(jsonPath("$.[0].memberId").value(equalTo(1)))
        .andExpect(jsonPath("$.[0].studentId").value(equalTo("12171707")));
  }

  @DisplayName("명예의 전당 조회 성공 200")
  @Test
  public void getHallOfFameMembers() throws Exception {

    // given
    List<HallOfFameDto> dtoList = new ArrayList<>();
    HallOfFameDto dto1 =
        new HallOfFameDto("홍길동", 1L, "12171707", 1, "컴퓨터공학", null, "자기소개", "email@google.com");
    dtoList.add(dto1);

    given(memberManageService.getHallOfFame()).willReturn(dtoList);

    // then
    mvc.perform(get("/members/hof"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].name").value(equalTo("홍길동")))
        .andExpect(jsonPath("$.[0].memberId").value(equalTo(1)))
        .andExpect(jsonPath("$.[0].studentId").value(equalTo("12171707")));
  }

  @DisplayName("비활동 이상 멤버 권한을 변경할 때 가능한 권한이면 204, 아니면 400을 반환한다.")
  @ParameterizedTest
  @ValueSource(strings = {"ADMIN", "SIGNING_UP"})
  public void updateApprovedMembers(String roleString) throws Exception {
    // given
    List<Long> memberIdList = List.of(1L);
    List<Member> members = List.of(MemberTest.deactivatedMember());

    if (roleString.equals("ADMIN")) {
      // when
      given(memberRepository.findAllById(memberIdList)).willReturn(members);
      // then
      mvc.perform(
              put("/members/approved/role")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(jsonOf(new UpdateRoleRequestDto(memberIdList, Role.ADMIN))))
          .andExpect(status().isNoContent());
    } else if (roleString.equals("SIGNING_UP")) {
      // when
      doThrow(new InvalidInputException())
          .when(memberManageService)
          .updateApprovedMembersRole(anyList(), any());
      // then
      mvc.perform(
              MockMvcRequestBuilders.put("/members/approved/role")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(jsonOf(new UpdateRoleRequestDto(memberIdList, Role.SIGNING_UP))))
          .andExpect(status().isBadRequest());
    }
  }

  @DisplayName("회장 연락처 정보를 불러온다")
  @Test
  public void getChiefContact() throws Exception {

    given(memberManageService.getChiefContact())
        .willReturn(new ContactDto("강지훈", "010-0000-0000", "my@email.com"));

    mvc.perform(get("/member/chief"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(equalTo("강지훈")))
        .andExpect(jsonPath("$.phoneNumber").value(equalTo("010-0000-0000")))
        .andExpect(jsonPath("$.email").value(equalTo("my@email.com")));
  }

  private String jsonOf(Object o) throws JsonProcessingException {
    return objectMapper.writeValueAsString(o);
  }
}
