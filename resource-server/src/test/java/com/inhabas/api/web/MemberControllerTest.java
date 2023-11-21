package com.inhabas.api.web;

import com.inhabas.api.auth.domain.oauth2.member.domain.service.MemberService;
import com.inhabas.api.auth.domain.oauth2.member.dto.ApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ContactDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.NotApprovedMemberManagementDto;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.usecase.AnswerService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.BASIC;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoSecureWebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AnswerService answerService;


    @DisplayName("(신입)미승인 멤버 정보 목록을 불러온다")
    @Test
    public void getUnapprovedMembers() throws Exception {

        // given
        List<NotApprovedMemberManagementDto> dtoList = new ArrayList<>();
        NotApprovedMemberManagementDto dto1 = new NotApprovedMemberManagementDto(
                "홍길동", 1L, "12171707",
                "010-1234-2345", "abc1234@gmail.com", "컴퓨터공학과");
        dtoList.add(dto1);

        given(memberService.getNotApprovedMembersBySearchAndRole(any())).willReturn(dtoList);
        given(memberService.getPagedDtoList(any(Pageable.class), eq(dtoList))).willReturn((List) dtoList);

        // then
        mvc.perform(get("/members/unapproved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value(equalTo("홍길동")))
                .andExpect(jsonPath("$.data[0].memberId").value(equalTo(1)))
                .andExpect(jsonPath("$.data[0].studentId").value(equalTo("12171707")))
                .andExpect(jsonPath("$.data[0].phoneNumber").value(equalTo("010-1234-2345")))
                .andExpect(jsonPath("$.data[0].email").value(equalTo("abc1234@gmail.com")))
                .andExpect(jsonPath("$.data[0].major").value(equalTo("컴퓨터공학과")));

    }

    @DisplayName("(신입)미승인 멤버 -> 비활동 멤버로 변경한다.")
    @Test
    public void passUnapprovedMembers() throws Exception {

        // 회원가입 이후 구현
    }

    @DisplayName("(신입)미승인 멤버 가입 거절 처리한다.")
    @Test
    public void failUnapprovedMembers() throws Exception {

        // 회원가입 이후 구현
    }

    @DisplayName("특정 신입 멤버 지원서를 조회한다.")
    @Test
    public void getUnapprovedMemberApplication() throws Exception {

        // given
        List<AnswerDto> dtoList = new ArrayList<>();
        AnswerDto dto1 = new AnswerDto(
                1, "안녕하세요. 예 안녕히계세요.");
        dtoList.add(dto1);

        given(answerService.getAnswers(any())).willReturn(dtoList);

        // then
        mvc.perform(get("/members/1/application"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].questionNo").value(equalTo(1)))
                .andExpect(jsonPath("$.[0].content").value(equalTo("안녕하세요. 예 안녕히계세요.")));

    }

    @DisplayName("비활동 이상 모든 멤버 목록을 조회한다.")
    @Test
    public void getApprovedMembers() throws Exception {

        // given
        List<ApprovedMemberManagementDto> dtoList = new ArrayList<>();
        ApprovedMemberManagementDto dto1 = new ApprovedMemberManagementDto(
                "홍길동", 1L, "12171707",
                "010-1234-2345", BASIC, 1, "컴퓨터공학과");
        dtoList.add(dto1);

        given(memberService.getApprovedMembersBySearchAndRole(any())).willReturn(dtoList);
        given(memberService.getPagedDtoList(any(Pageable.class), eq(dtoList))).willReturn((List) dtoList);

        // then
        mvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value(equalTo("홍길동")))
                .andExpect(jsonPath("$.data[0].memberId").value(equalTo(1)))
                .andExpect(jsonPath("$.data[0].studentId").value(equalTo("12171707")))
                .andExpect(jsonPath("$.data[0].phoneNumber").value(equalTo("010-1234-2345")))
                .andExpect(jsonPath("$.data[0].generation").value(equalTo(1)))
                .andExpect(jsonPath("$.data[0].major").value(equalTo("컴퓨터공학과")));

    }

    @DisplayName("비활동 이상 멤버 권한 수정한다.")
    @Test
    public void updateApprovedMembers() throws Exception {

        // 회원가입 이후 구현
    }

    @DisplayName("회장 연락처 정보를 불러온다")
    @Test
    public void getChiefContact() throws Exception {

        given(memberService.getChiefContact())
                .willReturn(new ContactDto("강지훈", "010-0000-0000","my@email.com"));

        mvc.perform(get("/member/chief"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(equalTo("강지훈")))
                .andExpect(jsonPath("$.phoneNumber").value(equalTo("010-0000-0000")))
                .andExpect(jsonPath("$.email").value(equalTo("my@email.com")));

    }

}
