package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.PROFESSOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.questionnaire.domain.Questionnaire;
import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;
import com.inhabas.api.domain.signUp.domain.entity.Answer;
import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.dto.SignUpDto;
import com.inhabas.api.domain.signUp.usecase.SignUpService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(SignUpController.class)
public class SignUpControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private SignUpService signUpService;

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("요청을 보낸 사용자가 회원가입을 했는지 확인한다.")
  @Test
  public void 사용자가_회원가입을_했는지_확인한다() throws Exception {
    // given
    Map<String, Boolean> check = Collections.singletonMap("check", true);
    given(signUpService.isSignedUp(any())).willReturn(true);

    // when
    String response =
        mvc.perform(get("/signUp/check"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(check));
  }

  @DisplayName("임시 저장했던 개인정보를 불러온다.")
  @Test
  public void 임시저장했던_개인정보를_불러온다() throws Exception {
    // given
    SignUpDto expectedSavedForm =
        SignUpDto.builder()
            .name("홍길동")
            .major("의예과")
            .phoneNumber("010-1234-5678")
            .studentId("12121212")
            .memberType(MemberType.UNDERGRADUATE)
            .grade(1)
            .build();

    given(signUpService.loadSignUpForm(any())).willReturn(expectedSavedForm);

    // when
    String response =
        mvc.perform(get("/signUp"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(expectedSavedForm));
  }

  @DisplayName("학생 회원가입 도중 개인정보를 저장한다.")
  @Test
  public void 학생_회원가입_도중_개인정보를_저장한다() throws Exception {
    // given
    SignUpDto signUpForm =
        SignUpDto.builder()
            .name("유동현")
            .major("컴퓨터공학과")
            .phoneNumber("010-0000-1111")
            .studentId("11112222")
            .memberType(MemberType.UNDERGRADUATE)
            .build();

    mvc.perform(
            post("/signUp")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(signUpForm)))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  @DisplayName("학생 개인정보를 빈칸으로 제출하면 안된다.")
  @Test
  public void 학생_개인정보를_빈칸으로_제출하면_안된다() throws Exception {
    // given
    SignUpDto signUpForm =
        SignUpDto.builder()
            .name("")
            .major("")
            .phoneNumber("")
            .studentId(null)
            .memberType(null)
            .build();

    String response =
        mvc.perform(
                post("/signUp")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(signUpForm)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("학생 개인정보 입력값이 정해진 범위를 초과하면 안된다.")
  @Test
  public void 학생_개인정보_입력값이_정해진_범위를_초과하면_안된다() throws Exception {
    // given
    SignUpDto signUpForm =
        SignUpDto.builder()
            .name("홍길동만세".repeat(10) + ".") // 50자까지만 가능
            .major("금융데이터처리, 블록체인학과.") // 50자가지만 가능
            .phoneNumber("8210-1111-1111") // ^(010)-\d{4}-\d{4}$
            .studentId("123123123")
            .memberType(MemberType.UNDERGRADUATE)
            .build();

    String response =
        mvc.perform(
                post("/signUp")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(signUpForm)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("교수 회원가입 도중 개인정보를 저장한다.")
  @Test
  public void 교수_회원가입_도중_개인정보를_저장한다() throws Exception {
    // given
    SignUpDto signUpForm =
        SignUpDto.builder()
            .name("유동현")
            .major("컴퓨터공학과")
            .phoneNumber("010-0000-1111")
            .studentId("12121212")
            .memberType(PROFESSOR)
            .build();

    mvc.perform(
            post("/signUp")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(signUpForm)))
        .andExpect(status().isNoContent())
        .andReturn();
  }

  @DisplayName("회원가입에 필요한 전공정보를 모두 가져온다.")
  @Test
  public void 회원가입에_필요한_전공정보를_모두_가져온다() throws Exception {
    // given
    MajorInfoDto majorInfo1 = new MajorInfoDto(1, "공과대학", "기계공학과");
    MajorInfoDto majorInfo2 = new MajorInfoDto(2, "자연과학대학", "수학과");
    MajorInfoDto majorInfo3 = new MajorInfoDto(3, "경영대학", "경영학과");
    List<MajorInfoDto> majorInfos =
        new ArrayList<>() {
          {
            add(majorInfo1);
            add(majorInfo2);
            add(majorInfo3);
          }
        };
    given(signUpService.getMajorInfo()).willReturn(majorInfos);

    // when
    String response =
        mvc.perform(get("/signUp/majorInfo"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(majorInfos));
  }

  @DisplayName("회원가입에 필요한 질문들을 가져온다.")
  @Test
  public void 회원가입에_필요한_질문들을_가져온다() throws Exception {
    // given
    ArrayList<QuestionnaireDto> questionnaireInDatabase =
        new ArrayList<>() {
          {
            add(new QuestionnaireDto(1L, "지원동기 및 목표를 기술해주세요."));
            add(new QuestionnaireDto(2L, "프로그래밍 관련 언어를 다루어 본 적이 있다면 적어주세요."));
            add(new QuestionnaireDto(3L, "빅데이터 관련 활동 혹은 공모전 관련 경험이 있다면 적어주세요."));
            add(new QuestionnaireDto(4L, "추후 희망하는 진로가 무엇이며, 동아리 활동이 진로에 어떠한 영향을 줄 것이라고 생각하나요?"));
            add(new QuestionnaireDto(5L, "어떤 경로로 IBAS를 알게 되셨나요?"));
          }
        };
    given(signUpService.getQuestionnaire()).willReturn(questionnaireInDatabase);

    // when
    String response =
        mvc.perform(get("/signUp/questionnaires"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(questionnaireInDatabase));
  }

  @DisplayName("임시저장했던 답변을 가져온다.")
  @Test
  public void 임시저장했던_답변을_가져온다() throws Exception {
    // given
    ArrayList<AnswerDto> savedDTOs =
        new ArrayList<>() {
          {
            add(new AnswerDto(1L, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new AnswerDto(2L, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new AnswerDto(3L, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new AnswerDto(4L, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
          }
        };
    given(signUpService.getAnswers(any())).willReturn(savedDTOs);

    // when
    String response =
        mvc.perform(get("/signUp/answers"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(savedDTOs));
  }

  @DisplayName("회원가입을 위한 답변을 저장한다.")
  @Test
  public void 회원가입을_위한_답변을_저장한다() throws Exception {
    // given
    Member member = MemberTest.signingUpMemberAfterProfile();
    ArrayList<Questionnaire> questionnaires =
        new ArrayList<>() {
          {
            add(new Questionnaire(1L, "질문 1"));
            add(new Questionnaire(2L, "질문 2"));
            add(new Questionnaire(3L, "질문 3"));
          }
        };

    ArrayList<Answer> submittedAnswers =
        new ArrayList<>() {
          {
            add(new Answer(member, questionnaires.get(0), "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new Answer(member, questionnaires.get(1), "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(
                new Answer(
                    member, questionnaires.get(2), "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
          }
        };

    // when then
    mvc.perform(
            post("/signUp/answers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(submittedAnswers)))
        .andExpect(status().isOk())
        .andReturn();
  }

  @DisplayName("회원가입을 완료처리한다.")
  @Test
  public void 회원가입을_완료처리한다() throws Exception {
    // given
    Member member = MemberTest.signingUpMemberAfterProfile();
    ArrayList<Questionnaire> questionnaires =
        new ArrayList<>() {
          {
            add(new Questionnaire(1L, "질문 1"));
            add(new Questionnaire(2L, "질문 2"));
            add(new Questionnaire(3L, "질문 3"));
          }
        };

    ArrayList<Answer> submittedAnswers =
        new ArrayList<>() {
          {
            add(new Answer(member, questionnaires.get(0), "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new Answer(member, questionnaires.get(1), "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(
                new Answer(
                    member, questionnaires.get(2), "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
          }
        };

    // when
    mvc.perform(
            put("/signUp")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(submittedAnswers)))
        .andExpect(status().isNoContent())
        .andReturn();
  }
}
