package com.inhabas.api.web;

import static com.inhabas.api.domain.member.domain.MemberTest.basicMember1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.domain.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.member.NoQueryParameterException;
import com.inhabas.api.domain.member.domain.entity.Answer;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.MemberType;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.domain.member.dto.SignUpDto;
import com.inhabas.api.domain.member.usecase.SignUpService;
import com.inhabas.api.domain.questionaire.dto.QuestionnaireDto;
import com.inhabas.testAnnotataion.DefaultWebMvcTest;
import com.inhabas.testAnnotataion.WithMockJwtAuthenticationToken;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@DefaultWebMvcTest(SignUpController.class)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignUpService signUpService;


    @DisplayName("학생 회원가입 도중 개인정보를 저장한다.")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 학생_회원가입_도중_개인정보를_저장한다() throws Exception {
        //given
        SignUpDto signUpForm = SignUpDto.builder()
                .name("유동현")
                .email("my@email.com")
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .memberId(new MemberId(11112222))
                .memberType(MemberType.UNDERGRADUATE)
                .build();

        mvc.perform(post("/signUp")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(signUpForm)))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private String jsonOf(Object response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response);
    }

    @DisplayName("학생 개인정보를 빈칸으로 제출하면 안된다.")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 학생_개인정보를_빈칸으로_제출하면_안된다() throws Exception {
        //given
        SignUpDto signUpForm = SignUpDto.builder()
                .name("")
                .email("")
                .major("")
                .phoneNumber("")
                .memberId(null)
                .memberType(null)
                .build();

        String response = mvc.perform(post("/signUp")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(signUpForm)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response.split("\n")).containsExactlyInAnyOrder(
                "[memberId](은)는 must not be null 입력된 값: [null]",
                "[major](은)는 must not be blank 입력된 값: []",
                "[name](은)는 must not be blank 입력된 값: []",
                "[phoneNumber](은)는 must match \"\\d{3}-\\d{4}-\\d{4}\" 입력된 값: []",
                "[memberType](은)는 must not be null 입력된 값: [null]");
    }

    @DisplayName("학생 개인정보 입력값이 정해진 범위를 초과하면 안된다.")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 학생_개인정보_입력값이_정해진_범위를_초과하면_안된다() throws Exception {
        //given
        SignUpDto signUpForm = SignUpDto.builder()
                .name("홍길동만세".repeat(5) + ".") // 25자까지만 가능
                .email("") // 상관없음.
                .major("금융데이터처리, 블록체인학과.") // 15자가지만 가능
                .phoneNumber("8210-1111-1111")
                .memberId(new MemberId(-1))
                .memberType(MemberType.UNDERGRADUATE)
                .build();

        String response = mvc.perform(post("/signUp")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(signUpForm)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response.split("\n")).containsExactlyInAnyOrder(
                "[memberId](은)는 must be greater than 0 입력된 값: [-1]",
                "[phoneNumber](은)는 must match \"\\d{3}-\\d{4}-\\d{4}\" 입력된 값: [8210-1111-1111]",
                "[name](은)는 length must be between 0 and 25 입력된 값: [홍길동만세홍길동만세홍길동만세홍길동만세홍길동만세.]",
                "[major](은)는 length must be between 0 and 15 입력된 값: [금융데이터처리, 블록체인학과.]");
    }

    @DisplayName("임시 저장했던 개인정보를 불러온다.")
    @Test
    @WithMockJwtAuthenticationToken(memberId = 12171652, memberRole = Role.ANONYMOUS)
    public void 임시저장했던_개인정보를_불러온다() throws Exception {
        //given
        OAuth2UserInfoAuthentication authentication =
                (OAuth2UserInfoAuthentication) SecurityContextHolder.getContext().getAuthentication();
        MemberId loginMemberId = (MemberId) authentication.getPrincipal();
        SignUpDto expectedSavedForm = SignUpDto.builder()
                .memberId(loginMemberId)
                .name("홍길동")
                .major("의예과")
                .phoneNumber("010-1234-5678")
                .email("my@email.com")
                .memberType(MemberType.UNDERGRADUATE)
                .build();

        given(signUpService.loadSignUpForm(loginMemberId, authentication)).willReturn(expectedSavedForm);

        //when
        String response = mvc.perform(get("/signUp"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        //then
        assertThat(response).isEqualTo(jsonOf(expectedSavedForm));
    }

//    @DisplayName("교수 회원가입 도중 개인정보를 저장한다.")
//    @Test
//    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
//    public void 교수_회원가입_도중_개인정보를_저장한다() throws Exception {
//        //given
//        ProfessorSignUpDto signUpForm = ProfessorSignUpDto.builder()
//                .name("유동현")
//                .email("my@email.com")
//                .major("컴퓨터공학과")
//                .phoneNumber("010-0000-1111")
//                .memberId(11112222)
//                .build();
//
//        mvc.perform(post("/signUp/professor")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonOf(signUpForm)))
//                .andExpect(status().isNoContent())
//                .andReturn();
//    }
//
//    @DisplayName("교수 개인정보를 빈칸으로 제출하면 안된다.")
//    @Test
//    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
//    public void 교수_개인정보를_빈칸으로_제출하면_안된다() throws Exception {
//        //given
//        ProfessorSignUpDto signUpForm = ProfessorSignUpDto.builder()
//                .name("")
//                .email("")
//                .major("")
//                .phoneNumber("")
//                .memberId(null)
//                .build();
//
//        String response = mvc.perform(post("/signUp/professor")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonOf(signUpForm)))
//                .andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse().getContentAsString(StandardCharsets.UTF_8);
//
//        assertThat(response).contains(
//                "[memberId](은)는 must not be null",
//                "[name](은)는 must not be blank",
//                "[major](은)는 must not be blank",
//                "[phoneNumber](은)는 must match \"\\d{3}-\\d{4}-\\d{4}\"");
//    }
//
//    @DisplayName("교수 개인정보 입력값이 정해진 범위를 초과하면 안된다.")
//    @Test
//    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
//    public void 교수_개인정보_입력값이_정해진_범위를_초과하면_안된다() throws Exception {
//        //given
//        ProfessorSignUpDto signUpForm = ProfessorSignUpDto.builder()
//                .name("홍길동만세".repeat(5) + ".") // 25자까지만 가능
//                .email("") // 상관없음.
//                .major("금융데이터처리, 블록체인학과.") // 15자가지만 가능
//                .phoneNumber("8210-1111-1111")
//                .memberId(-1)
//                .build();
//
//        String response = mvc.perform(post("/signUp/student")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonOf(signUpForm)))
//                .andExpect(status().isBadRequest())
//                .andDo(print()) // 지울것
//                .andReturn()
//                .getResponse().getContentAsString(StandardCharsets.UTF_8);
//
//        assertThat(response).contains(
//                "[name](은)는 length must be between 0 and 25",
//                "[phoneNumber](은)는 must match \"\\d{3}-\\d{4}-\\d{4}\"",
//                "[major](은)는 length must be between 0 and 15");
//    }

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
        given(signUpService.getQuestionnaire()).willReturn(questionnaireInDatabase);

        //when
        String response = mvc.perform(get("/signUp/questionnaires"))
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
        given(signUpService.getAnswers(any())).willReturn(savedDTOs);

        //when
        String response = mvc.perform(get("/signUp/answers"))
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
        Member member = basicMember1();
        ArrayList<Answer> submittedAnswers = new ArrayList<>() {{
            add(new Answer(member, 1, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new Answer(member, 2, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new Answer(member, 3, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new Answer(member, 4, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};

        //when then
        mvc.perform(post("/signUp/answers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(submittedAnswers)))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @DisplayName("회원가입을 완료처리한다.")
    @Test
    @WithMockJwtAuthenticationToken(memberId = 12171652, memberRole = Role.ANONYMOUS)
    public void 회원가입을_완료처리한다() throws Exception {
        //when
        mvc.perform(put("/signUp").with(csrf()))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @DisplayName("회원가입에 필요한 전공정보를 모두 가져온다.")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 회원가입에_필요한_전공정보를_모두_가져온다() throws Exception {
        //given
        MajorInfoDto majorInfo1 = new MajorInfoDto(1, "공과대학", "기계공학과");
        MajorInfoDto majorInfo2 = new MajorInfoDto(2, "자연과학대학", "수학과");
        MajorInfoDto majorInfo3 = new MajorInfoDto(3, "경영대학", "경영학과");
        List<MajorInfoDto> majorInfos = new ArrayList<>() {{
            add(majorInfo1);
            add(majorInfo2);
            add(majorInfo3);
        }};
        given(signUpService.getMajorInfo()).willReturn(majorInfos);

        //when
        String response = mvc.perform(get("/signUp/majorInfo"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        //then
        assertThat(response).isEqualTo(jsonOf(majorInfos));
    }

    @DisplayName("학번 중복 검사 결과, 중복된다")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void ID_중복_검사_중복됨() throws Exception {
        //given
        given(signUpService.validateFieldsDuplication(any(MemberDuplicationQueryCondition.class))).willReturn(true);

        //when
        mvc.perform(get("/signUp/isDuplicated")
                .param("memberId", "12171652"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
    }

    @DisplayName("학번 중복 검사 결과, 중복되지 않는다.")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void ID_중복_검사_중복되지_않는다() throws Exception {
        //given
        given(signUpService.validateFieldsDuplication(any(MemberDuplicationQueryCondition.class))).willReturn(false);

        //when
        mvc.perform(get("/signUp/isDuplicated")
                        .param("memberId", "12171652"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();
    }

    @DisplayName("핸드폰 중복 검사 결과, 중복된다.")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 핸드폰_중복_검사_중복된다() throws Exception {
        //given
        given(signUpService.validateFieldsDuplication(any(MemberDuplicationQueryCondition.class))).willReturn(true);

        //when
        mvc.perform(get("/signUp/isDuplicated")
                        .param("phoneNumber", "010-0000-0000"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
    }

    @DisplayName("핸드폰 중복 검사 결과, 중복되지 않는다.")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 핸드폰_중복_검사_중복되지_않는다() throws Exception {
        //given
        given(signUpService.validateFieldsDuplication(any(MemberDuplicationQueryCondition.class))).willReturn(false);

        //when
        mvc.perform(get("/signUp/isDuplicated")
                        .param("phoneNumber", "010-0000-0000"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andReturn();
    }


    @DisplayName("핸드폰 중복 검사 결과, 번호 형식이 잘못되면 400 반환")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 핸드폰_양식이_잘못된_경우_400() throws Exception {
        //given
        given(signUpService.validateFieldsDuplication(any(MemberDuplicationQueryCondition.class))).willReturn(true);

        //when
        mvc.perform(get("/signUp/isDuplicated")
                        .param("phoneNumber", "0101-0000-0000"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @DisplayName("핸드폰과 학번이 동시에 중복검사")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 핸드폰과_학번이_동시에_중복검사() throws Exception {
        given(signUpService.validateFieldsDuplication(any(MemberDuplicationQueryCondition.class))).willReturn(true);

        mvc.perform(get("/signUp/isDuplicated")
                        .param("phoneNumber", "010-0000-0000")
                        .param("memberId", "12171652"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @DisplayName("중복 검사 시 핸드폰과 학번 둘 다 안넘어오면 400 반환")
    @Test
    @WithMockJwtAuthenticationToken(memberRole = Role.ANONYMOUS)
    public void 중복_검사_시_핸드폰과_학번_둘_다_안넘어오면_400_에러() throws Exception {
        given(signUpService.validateFieldsDuplication(any(MemberDuplicationQueryCondition.class)))
                .willThrow(NoQueryParameterException.class);

        mvc.perform(get("/signUp/isDuplicated"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
