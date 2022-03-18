package com.inhabas.integrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.ApiApplication;
import com.inhabas.api.domain.member.MajorInfo;
import com.inhabas.api.domain.member.MajorInfoRepository;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.domain.questionaire.Questionnaire;
import com.inhabas.api.domain.questionaire.QuestionnaireRepository;
import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.SignUpDto;
import com.inhabas.api.security.domain.AuthUser;
import com.inhabas.api.security.domain.AuthUserNotFoundException;
import com.inhabas.api.security.domain.AuthUserRepository;
import com.inhabas.api.security.jwtUtils.TokenProvider;
import com.inhabas.api.service.member.MemberNotFoundException;
import com.inhabas.testConfig.CustomSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomSpringBootTest(classes = ApiApplication.class)
public class SignUpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private TokenProvider tokenProvider;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AuthUserRepository authUserRepository;
    @Autowired private QuestionnaireRepository questionnaireRepository;
    @Autowired private MajorInfoRepository majorInfoRepository;
    @Autowired private MemberRepository memberRepository;

    private Integer authUserId;

    @BeforeEach
    public void setUp() {
        authUserId = authUserRepository.save(new AuthUser("google", "my@gmail.com")).getId();
    }

    @Test
    public void 기존_일반회원_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(Role.BASIC_MEMBER);
    }
    @Test
    public void 비활동회원_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(Role.DEACTIVATED_MEMBER);
    }
    @Test
    public void 미승인회원_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(Role.NOT_APPROVED_MEMBER);
    }
    @Test
    public void 회장단_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(Role.EXECUTIVES);
    }
    @Test
    public void 관리자_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(Role.ADMIN);
    }

    @Test
    public void OAuth2_인증_후_비회원_신규_학생_회원가입() throws Exception {

        //given
        면접질문_설정();
        전공정보_설정();


        /* 유동현은 IBAS 에 회원 가입하기 위해
        소셜 로그인 후 회원 가입용 임시 토큰을 발급 받았다.*/
        String token = tokenProvider.createJwtToken(authUserId, Role.ANONYMOUS.toString(), null).getAccessToken();

        /* OAuth2 인증이 완료되면 자동으로 회원가입 페이지로 리다이렉트 된다.
        이 때, 회원가입을 완료하지 않고 임시저장했던 프로필 정보가 있는지 불러오길 시도하지만
        신규회원 가입이기 때문에, 소셜 이메일을 제외하고는 아무것도 받지 못한다. */
        mockMvc.perform(get("/signUp/student").with(accessToken(token)))
                        .andExpect(status().isOk())
                        .andExpect(content().string("{\"email\":\"my@gmail.com\"}"));

        /* 개인정보 입력을 위해, 전공 정보들이 로딩된다. */
        mockMvc.perform(get("/signUp/majorInfo").with(accessToken(token)))
                .andExpect(status().isOk());

        /* 프로필 입력 중에 학번이 중복되는 지 검사한다.
        중복되는 학번이 없다고 응답한다. */
        mockMvc.perform(get("/signUp/isDuplicated")
                        .param("memberId", "12171652"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        /* 프로필 입력 중에 전화번호가 중복되는 지 검사한다.
        중복되는 전화번호가 없다고 응답한다. */
        mockMvc.perform(get("/signUp/isDuplicated")
                        .param("phone", "010-0000-0000"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        /* 프로필 입력을 완료하여 다음 버튼을 누르면, 개인정보가 임시저장된다. */
        mockMvc.perform(post("/signUp/student").with(accessToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(SignUpDto.builder()
                                .email("my@gmail.com")
                                .memberId(12171652)
                                .name("유동현")
                                .phoneNumber("010-0000-0000")
                                .major("컴퓨터공학과")
                                .build())))
                .andExpect(status().isNoContent());

        /* 다음 페이지에서 면접용 질문 리스트가 로딩된다. */
        String questionList = mockMvc.perform(get("/signUp/questionnaire").with(accessToken(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(questionList).isEqualTo("[" +
                "{\"id\":1,\"question\":\"지원동기 및 목표를 기술해주세요.\"}," +
                "{\"id\":2,\"question\":\"프로그래밍 관련 언어를 다루어 본 적이 있다면 적어주세요.\"}," +
                "{\"id\":3,\"question\":\"빅데이터 관련 활동 혹은 공모전 관련 경험이 있다면 적어주세요.\"}," +
                "{\"id\":4,\"question\":\"추후 희망하는 진로가 무엇이며, 동아리 활동이 진로에 어떠한 영향을 줄 것이라고 생각하나요?\"}," +
                "{\"id\":5,\"question\":\"어떤 경로로 IBAS를 알게 되셨나요?\"}" +
                "]");

        /* 기존에 작성했던 답변을 가져오지만, 기존 답변이 없다. */
        mockMvc.perform(get("/signUp/answer").with(accessToken(token)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        /* 면접 질문에 대답하고 임시저장 버튼을 클릭한다. */
        mockMvc.perform(post("/signUp/answer").with(accessToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Arrays.asList(
                                new AnswerDto(1, "몰랑"),
                                new AnswerDto(2, "아몰랑"),
                                new AnswerDto(3, "아아몰랑"),
                                new AnswerDto(4, "아모른다구"),
                                new AnswerDto(5, "ㅎ")))))
                .andExpect(status().isNoContent());

        /* 회원가입 신청을 완료한다. */
        mockMvc.perform(put("/signUp/finish").with(accessToken(token)))
                .andExpect(status().isNoContent());


        //then
        Member 유동현 = memberRepository.findById(12171652).orElseThrow(MemberNotFoundException::new);
        assertThat(유동현.getIbasInformation().getRole()).isEqualTo(Role.NOT_APPROVED_MEMBER);
        AuthUser 유동현_소셜_계정 = authUserRepository.findById(authUserId).orElseThrow(AuthUserNotFoundException::new);
        assertThat(유동현_소셜_계정.getProfileId()).isEqualTo(12171652);
        assertThat(유동현_소셜_계정.hasJoined()).isEqualTo(true);
    }

//    @Test
//    public void OAuth2_인증_후_비회원_신규_교수_회원가입() throws Exception {
//
//        //given
//        전공정보_설정();
//
//        /* 유동현 교수는 IBAS 에 회원 가입하기 위해
//        소셜 로그인 후 회원 가입용 임시 토큰을 발급 받았다.*/
//        String token = tokenProvider.createJwtToken(authUserId, Role.ANONYMOUS.toString(), null).getAccessToken();
//
//        /* OAuth2 인증이 완료되면 자동으로 회원가입 페이지로 리다이렉트 된다. */
//
//        /* 개인정보 입력을 위해, 전공 정보들이 로딩된다. */
//        String majorList = mockMvc.perform(get("/signUp/majorInfo").with(accessToken(token)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
//        assertThat(majorList).isEqualTo("[" +
//                "{\"id\":1,\"college\":\"공과대학\",\"major\":\"기계공학과\"}," +
//                "{\"id\":2,\"college\":\"자연과학대학\",\"major\":\"수학과\"}," +
//                "{\"id\":3,\"college\":\"경영대학\",\"major\":\"경영학과\"}" +
//                "]");
//
//        /* 프로필 입력 중에 학번이 중복되는 지 검사한다.
//        중복되는 학번이 없다고 응답한다. */
//        mockMvc.perform(get("/signUp/isDuplicated")
//                        .param("memberId", "228761"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("false"));
//
//        /* 프로필 입력 중에 전화번호가 중복되는 지 검사한다.
//        중복되는 전화번호가 없다고 응답한다. */
//        mockMvc.perform(get("/signUp/isDuplicated")
//                        .param("phone", "010-0000-0000"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("false"));
//
//        /* 프로필 입력을 완료하여 다음 버튼을 누르면, 개인정보가 임시저장된다. */
//        mockMvc.perform(post("/signUp/professor").with(accessToken(token))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonOf(ProfessorSignUpDto.builder()
//                                .email("my@gmail.com")
//                                .memberId(228761)
//                                .name("유동현")
//                                .phoneNumber("010-0000-0000")
//                                .major("컴퓨터공학과")
//                                .build())))
//                .andExpect(status().isNoContent());
//
//
//        /* 회원가입 신청을 완료한다. */
//        mockMvc.perform(put("/signUp/finish").with(accessToken(token)))
//                .andExpect(status().isNoContent());
//
//
//        //then
//        Member 유동현_교수 = memberRepository.findById(228761).orElseThrow(MemberNotExistException::new);
//        assertThat(유동현_교수.getIbasInformation().getRole()).isEqualTo(Role.NOT_APPROVED_MEMBER);
//        AuthUser 유동현_소셜_계정 = authUserRepository.findById(authUserId).orElseThrow(AuthUserNotFoundException::new);
//        assertThat(유동현_소셜_계정.getProfileId()).isEqualTo(228761);
//        assertThat(유동현_소셜_계정.hasJoined()).isEqualTo(true);
//    }

    private void forbiddenWhenAccessEverySignUpApi(Role role) throws Exception {
        String token = tokenProvider.createJwtToken(authUserId, role.toString(), null).getAccessToken();

        mockMvc.perform(get("/signUp/student").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/signUp/student").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/signUp/professor").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/signUp/answer").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/signUp/answer").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/signUp/questionnaire").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/signUp/majorInfo").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/signUp/isDuplicated").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(put("/signUp/finish").with(accessToken(token)))
                .andExpect(status().isForbidden());
    }
    public static RequestPostProcessor accessToken(String accessToken) {
        return request -> {
            request.addHeader("Authorization", "Bearer " + accessToken);
            return request;
        };
    }

    private void 면접질문_설정() {
        questionnaireRepository.saveAll(
                Arrays.asList(
                        new Questionnaire(1, "지원동기 및 목표를 기술해주세요."),
                        new Questionnaire(2, "프로그래밍 관련 언어를 다루어 본 적이 있다면 적어주세요."),
                        new Questionnaire(3, "빅데이터 관련 활동 혹은 공모전 관련 경험이 있다면 적어주세요."),
                        new Questionnaire(4, "추후 희망하는 진로가 무엇이며, 동아리 활동이 진로에 어떠한 영향을 줄 것이라고 생각하나요?"),
                        new Questionnaire(5, "어떤 경로로 IBAS를 알게 되셨나요?")));
    }
    private void 전공정보_설정() {
        majorInfoRepository.saveAll(
                Arrays.asList(
                        new MajorInfo("공과대학", "기계공학과"),
                        new MajorInfo("자연과학대학", "수학과"),
                        new MajorInfo("경영대학", "경영학과"))
        );
    }

    private String jsonOf(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

}
