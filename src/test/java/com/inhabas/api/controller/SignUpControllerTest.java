package com.inhabas.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.annotataion.WithMockJwtAuthenticationToken;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.DetailSignUpForm;
import com.inhabas.api.dto.signUp.StudentSignUpForm;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.api.testConfig.DefaultWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultWebMvcTest(SignUpController.class)
public class SignUpControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @DisplayName("회원가입 도중 개인정보를 저장한다.")
    @Test
    @WithMockJwtAuthenticationToken
    public void 회원가입_도중_개인정보를_저장한다() throws Exception {
        //given
        StudentSignUpForm signUpForm = StudentSignUpForm.builder()
                .name("유동현")
                .grade(3)
                .semester(2)
                .email("my@email.com")
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .studentId(11112222)
                .isProfessor(false)
                .build();

        mvc.perform(post("/signUp/student")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpForm)))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @DisplayName("개인정보를 빈칸으로 제출하면 안된다.")
    @Test
    @WithMockJwtAuthenticationToken
    public void 개인정보를_빈칸으로_제출하면_안된다() throws Exception {
        //given
        StudentSignUpForm signUpForm = StudentSignUpForm.builder()
                .name("")
                .grade(null)
                .semester(null)
                .email("")
                .major("")
                .phoneNumber("")
                .studentId(null)
                .isProfessor(false)
                .build();

        String response = mvc.perform(post("/signUp/student")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpForm)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).contains(
                "[studentId](은)는 must not be null",
                "[grade](은)는 must not be null",
                "[name](은)는 must not be blank",
                "[major](은)는 must not be blank",
                "[phoneNumber](은)는 must match \"\\d{3}-\\d{4}-\\d{4}\"",
                "[semester](은)는 must not be null");
    }

    @Test
    @WithMockJwtAuthenticationToken
    public void 개인정보_입력값이_정해진_범위를_초과하면_안된다() throws Exception {
        //given
        StudentSignUpForm signUpForm = StudentSignUpForm.builder()
                .name("홍길동만세".repeat(5) + ".") // 25자까지만 가능
                .grade(6) // 5학년까지만 가능
                .semester(3) // 2학기가지만 가능
                .email("") // 상관없음.
                .major("금융데이터처리, 블록체인학과.") // 15자가지만 가능
                .phoneNumber("8210-1111-1111")
                .studentId(-1)
                .isProfessor(false)
                .build();

        String response = mvc.perform(post("/signUp/student")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpForm)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).contains(
                "[semester](은)는 must be less than or equal to 2",
                "[grade](은)는 must be less than or equal to 5",
                "[name](은)는 length must be between 0 and 25",
                "[phoneNumber](은)는 must match \"\\d{3}-\\d{4}-\\d{4}\"",
                "[major](은)는 length must be between 0 and 15");
    }

    @DisplayName("임시 저장했던 개인정보를 불러온다.")
    @Test
    @WithMockJwtAuthenticationToken(memberId = 12171652, memberRole = Role.ANONYMOUS)
    public void 임시저장했던_개인정보를_불러온다() throws Exception {
        //given
        DetailSignUpForm expectedSavedForm = DetailSignUpForm.builder()
                .memberId(12171652)
                .name("홍길동")
                .grade(1)
                .semester(1)
                .major("의예과")
                .phoneNumber("010-1234-5678")
                .isProfessor(false)
                .email("my@email.com")
                .build();

        given(memberService.loadSignUpForm(12171652, "my@email.com")).willReturn(expectedSavedForm);

        //when
        String response = mvc.perform(get("/signUp/student"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        //then
        assertThat(response).isEqualTo(objectMapper.writeValueAsString(expectedSavedForm));

    }
}
