package com.inhabas.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.annotataion.WithMockJwtAuthenticationToken;
import com.inhabas.api.domain.MemberTest;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.domain.questionaire.Answer;
import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.DetailSignUpDto;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import com.inhabas.api.dto.signUp.StudentSignUpDto;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.api.service.questionnaire.AnswerService;
import com.inhabas.api.service.questionnaire.QuestionnaireService;
import com.inhabas.api.testConfig.DefaultWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultWebMvcTest(SignUpController.class)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private QuestionnaireService questionnaireService;

    @MockBean
    private AnswerService answerService;

    @DisplayName("회원가입 도중 개인정보를 저장한다.")
    @Test
    @WithMockJwtAuthenticationToken
    public void 회원가입_도중_개인정보를_저장한다() throws Exception {
        //given
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
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
                        .content(jsonOf(signUpForm)))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private String jsonOf(Object response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response);
    }

    @DisplayName("개인정보를 빈칸으로 제출하면 안된다.")
    @Test
    @WithMockJwtAuthenticationToken
    public void 개인정보를_빈칸으로_제출하면_안된다() throws Exception {
        //given
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
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
                        .content(jsonOf(signUpForm)))
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
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
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
                        .content(jsonOf(signUpForm)))
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
        DetailSignUpDto expectedSavedForm = DetailSignUpDto.builder()
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
        assertThat(response).isEqualTo(jsonOf(expectedSavedForm));
    }

    @DisplayName("회원가입에 필요한 질문들을 가져온다.")
    @Test
    @WithMockJwtAuthenticationToken(memberId = 12171652, memberRole = Role.ANONYMOUS)
    public void 회원가입에_필요한_질문들을_가져온다() throws Exception {
        //given
        ArrayList<QuestionnaireDto> questionnaireInDatabase = new ArrayList<>(){{
            add(new QuestionnaireDto(1, "지원동기 및 목표를 기술해주세요."));
            add(new QuestionnaireDto(2, "프로그래밍 관련 언어를 다루어 본 적이 있다면 적어주세요."));
            add(new QuestionnaireDto(3, "빅데이터 관련 활동 혹은 공모전 관련 경험이 있다면 적어주세요."));
            add(new QuestionnaireDto(4, "추후 희망하는 진로가 무엇이며, 동아리 활동이 진로에 어떠한 영향을 줄 것이라고 생각하나요?"));
            add(new QuestionnaireDto(5, "어떤 경로로 IBAS를 알게 되셨나요?"));
        }};
        given(questionnaireService.getQuestionnaire()).willReturn(questionnaireInDatabase);

        //when
        String response = mvc.perform(get("/signUp/questionnaire"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        //then
        assertThat(response).isEqualTo(jsonOf(questionnaireInDatabase));
    }

    @DisplayName("임시저장했던 답변을 가져온다.")
    @Test
    @WithMockJwtAuthenticationToken(memberId = 12171652, memberRole = Role.ANONYMOUS)
    public void 임시저장했던_답변을_가져온다() throws Exception {
        //given
        ArrayList<AnswerDto> savedDTOs = new ArrayList<>() {{
            add(new AnswerDto(1, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new AnswerDto(2, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new AnswerDto(3, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new AnswerDto(4, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};
        given(answerService.getAnswers(anyInt())).willReturn(savedDTOs);

        //when
        String response = mvc.perform(get("/signUp/answer"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        //then
        assertThat(response).isEqualTo(jsonOf(savedDTOs));
    }

    @DisplayName("회원가입을 위한 답변을 저장한다.")
    @Test
    @WithMockJwtAuthenticationToken(memberId = 12171652, memberRole = Role.ANONYMOUS)
    public void 회원가입을_위한_답변을_저장한다() throws Exception {
        //given
        ArrayList<Answer> submittedAnswers = new ArrayList<>() {{
            add(new Answer(MemberTest.MEMBER1, 1, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new Answer(MemberTest.MEMBER1, 2, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new Answer(MemberTest.MEMBER1, 3, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new Answer(MemberTest.MEMBER1, 4, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};

        //when then
        mvc.perform(post("/signUp/answer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(submittedAnswers)))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
