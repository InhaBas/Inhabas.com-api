package com.inhabas.api.domain.signUp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.ApiApplication;
import com.inhabas.api.auth.domain.oauth2.CustomOAuth2User;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.MajorInfo;
import com.inhabas.api.auth.domain.oauth2.majorInfo.repository.MajorInfoRepository;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.service.MemberService;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoFactory;
import com.inhabas.api.auth.domain.token.TokenUtil;
import com.inhabas.api.domain.questionnaire.domain.Questionnaire;
import com.inhabas.api.domain.questionnaire.repository.QuestionnaireRepository;
import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.dto.SignUpDto;
import com.inhabas.api.domain.signUpSchedule.domain.entity.SignUpSchedule;
import com.inhabas.api.domain.signUpSchedule.repository.SignUpScheduleRepository;
import com.inhabas.testAnnotataion.CustomSpringBootTest;
import com.inhabas.testAnnotataion.WithMockJwtAuthenticationToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomSpringBootTest(classes = ApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignUpIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private MajorInfoRepository majorInfoRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SignUpScheduleRepository scheduleRepository;

    private static final String ROLE_PREFIX = "ROLE_";

    private String token;

    @BeforeAll
    public void setUp() {
        면접질문_설정();
        전공정보_설정();
        회원가입_가능한_기간();
        token = OAuth인증된_JWT_TOKEN(SIGNING_UP);
    }


    @Test
    @Transactional
    @WithMockJwtAuthenticationToken(memberRole = NOT_APPROVED)
    public void 미승인회원_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(NOT_APPROVED);
    }

    @Test
    @Transactional
    @WithMockJwtAuthenticationToken(memberRole = DEACTIVATED)
    public void 비활동회원_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(DEACTIVATED);
    }

    @Test
    @Transactional
    @WithMockJwtAuthenticationToken(memberRole = BASIC)
    public void 기존_일반회원_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(BASIC);
    }

    @Test
    @Transactional
    @WithMockJwtAuthenticationToken(memberRole = EXECUTIVES)
    public void 회장단_회원가입_비정상_접근() throws Exception {
        forbiddenWhenAccessEverySignUpApi(EXECUTIVES);
    }

    @Test
    @Transactional
    @WithMockJwtAuthenticationToken(memberRole = SIGNING_UP)
    public void 회원가입_기간이_아닙니다() throws Exception {
        /* 유동현은 IBAS 에 회원 가입하기 위해
        소셜 로그인 후 회원 가입용 임시 토큰을 발급 받았다.
        회원가입이 불가능한 때에 요청을 보낸경우 */
        scheduleRepository.deleteAll();
        회원가입_불가능한_기간();

        /* OAuth2 인증이 완료되면 자동으로 회원가입 페이지로 리다이렉트 된다.
        이 때, 회원가입을 완료하지 않고 임시저장했던 프로필 정보가 있는지 불러온다. */
        String response = mockMvc.perform(get("/signUp").with(accessToken(token)))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(response).isEqualTo("회원가입 기간이 아닙니다.");
    }

    @Test
    @Transactional
    @WithMockJwtAuthenticationToken(memberRole = SIGNING_UP)
    public void OAuth2_인증_후_비회원_신규_학생_회원가입() throws Exception {
        /* 유동현은 IBAS 에 회원 가입하기 위해
        소셜 로그인 후 회원 가입용 임시 토큰을 발급 받았다.*/

        /* OAuth2 인증이 완료되면 자동으로 회원가입 페이지로 리다이렉트 된다.
        이 때, 회원가입을 완료하지 않고 임시저장했던 프로필 정보가 있는지 불러오길 시도하지만
        신규회원 가입이기 때문에, 소셜 이메일을 제외하고는 아무것도 받지 못한다. */
        mockMvc.perform(get("/signUp").with(accessToken(token)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"name\":null,\"major\":null,\"phoneNumber\":null,\"studentId\":null,\"memberType\":null,\"grade\":null}"));

        /* 개인정보 입력을 위해, 전공 정보들이 로딩된다. */
        mockMvc.perform(get("/signUp/majorInfo").with(accessToken(token)))
                .andExpect(status().isOk());

        /* 프로필 입력을 완료하여 다음 버튼을 누르면, 개인정보가 임시저장된다. */
        mockMvc.perform(post("/signUp").with(accessToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(SignUpDto.builder()
                                .name("유동현")
                                .major("컴퓨터공학과")
                                .phoneNumber("010-0000-0000")
                                .studentId("12171652")
                                .memberType(MemberType.UNDERGRADUATE)
                                .grade(1)
                                .build())))
                .andExpect(status().isNoContent());

        /* 다음 페이지에서 면접용 질문 리스트가 로딩된다. */
        String questionList = mockMvc.perform(get("/signUp/questionnaires").with(accessToken(token)))
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
        mockMvc.perform(get("/signUp/answers").with(accessToken(token)))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"questionId\":1,\"content\":null},{\"questionId\":2,\"content\":null},{\"questionId\":3,\"content\":null},{\"questionId\":4,\"content\":null},{\"questionId\":5,\"content\":null}]"));

        /* 면접 질문에 대답하고 임시저장 버튼을 클릭한다. */
        mockMvc.perform(post("/signUp/answers").with(accessToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Arrays.asList(
                                new AnswerDto(1L, "몰랑"),
                                new AnswerDto(2L, "아몰랑"),
                                new AnswerDto(3L, "아아몰랑"),
                                new AnswerDto(4L, "아모른다구"),
                                new AnswerDto(5L, "ㅎ")))))
                .andExpect(status().isOk());

        /* 회원가입 신청을 완료한다. */
        mockMvc.perform(put("/signUp").with(accessToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(Arrays.asList(
                                new AnswerDto(1L, ""),
                                new AnswerDto(2L, "아몰랑"),
                                new AnswerDto(3L, "아아몰랑"),
                                new AnswerDto(4L, "아모른다구"),
                                new AnswerDto(5L, "ㅎ")))))
                .andExpect(status().isNoContent());


        //then
        Member 유동현 = memberRepository.findByProviderAndUid(OAuth2Provider.NAVER, new UID("N8ojJQXxxSxtO0CmEH3xtt5Y6ER09UEsRozkpbGAdOI")).orElseThrow();
        assertThat(유동현.getIbasInformation().getRole()).isEqualTo(NOT_APPROVED);

    }

    @Test
    @Transactional
    @WithMockJwtAuthenticationToken(memberRole = SIGNING_UP)
    public void OAuth2_인증_후_비회원_신규_교수_회원가입() throws Exception {
        /* 유동현 교수는 IBAS 에 회원 가입하기 위해
        소셜 로그인 후 회원 가입용 임시 토큰을 발급 받았다.*/

        /* OAuth2 인증이 완료되면 자동으로 회원가입 페이지로 리다이렉트 된다. */

        /* 개인정보 입력을 위해, 전공 정보들이 로딩된다. */
        String majorList = mockMvc.perform(get("/signUp/majorInfo").with(accessToken(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(majorList).isEqualTo("[" +
                "{\"id\":1,\"college\":\"공과대학\",\"major\":\"기계공학과\"}," +
                "{\"id\":2,\"college\":\"자연과학대학\",\"major\":\"수학과\"}," +
                "{\"id\":3,\"college\":\"경영대학\",\"major\":\"경영학과\"}" +
                "]");

        /* 프로필 입력을 완료하여 다음 버튼을 누르면, 개인정보가 임시저장된다. */
        mockMvc.perform(post("/signUp").with(accessToken(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(SignUpDto.builder()
                                .grade(1)
                                .studentId("228761")
                                .name("유동현")
                                .phoneNumber("010-0000-0000")
                                .major("컴퓨터공학과")
                                .memberType(MemberType.PROFESSOR)
                                .build())))
                .andExpect(status().isNoContent());


        /* 회원가입 신청을 완료한다. */
        mockMvc.perform(put("/signUp").with(accessToken(token)))
                        .andExpect(status().isNoContent());

        //then
        Member 유동현_교수 = memberRepository.findByProviderAndUid(OAuth2Provider.NAVER, new UID("N8ojJQXxxSxtO0CmEH3xtt5Y6ER09UEsRozkpbGAdOI")).orElseThrow();
        assertThat(유동현_교수.getIbasInformation().getRole()).isEqualTo(NOT_APPROVED);
    }

    private void forbiddenWhenAccessEverySignUpApi(Role role) throws Exception {
        //given
        String token = OAuth인증된_JWT_TOKEN(role);

        mockMvc.perform(get("/signUp").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/signUp").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/signUp/answers").with(accessToken(token)))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/signUp/answers").with(accessToken(token)))
                .andExpect(status().isForbidden());
    }


    // setUp
    public static RequestPostProcessor accessToken(String accessToken) {
        return request -> {
            request.addHeader("Authorization", "Bearer " + accessToken);
            return request;
        };
    }

    private String OAuth인증된_JWT_TOKEN(Role role) {
        List<? extends GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(ROLE_PREFIX+ role.toString()));
        Map<String, Object> nameAttributeKey = Map.of(
                "message", "success",
                "response", Map.of(
                        "id", "N8ojJQXxxSxtO0CmEH3xtt5Y6ER09UEsRozkpbGAdOI",
                        "profile_image", "https://ssl.pstatic.net/static/pwe/address/img_profile.png",
                        "email", "5177jsh@naver.com",
                        "name", "조승현"
                )
        );
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("NAVER", nameAttributeKey);
        memberService.updateSocialAccountInfo(oAuth2UserInfo);
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(grantedAuthorities, nameAttributeKey, "response", 1L);

        return tokenUtil.createAccessToken(new OAuth2AuthenticationToken(customOAuth2User, grantedAuthorities, "NAVER"));

    }

    private void 면접질문_설정() {
        questionnaireRepository.saveAll(
                Arrays.asList(
                        new Questionnaire(1L, "지원동기 및 목표를 기술해주세요."),
                        new Questionnaire(2L, "프로그래밍 관련 언어를 다루어 본 적이 있다면 적어주세요."),
                        new Questionnaire(3L, "빅데이터 관련 활동 혹은 공모전 관련 경험이 있다면 적어주세요."),
                        new Questionnaire(4L, "추후 희망하는 진로가 무엇이며, 동아리 활동이 진로에 어떠한 영향을 줄 것이라고 생각하나요?"),
                        new Questionnaire(5L, "어떤 경로로 IBAS를 알게 되셨나요?")));
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

    private void 회원가입_가능한_기간() {
        LocalDateTime now = LocalDateTime.now();
        scheduleRepository.save(
                new SignUpSchedule(1, now.minusDays(1L), now.plusDays(1L), now.plusDays(1L), now.plusDays(2L), now.plusDays(3L)));
    }

    private void 회원가입_불가능한_기간() {
        LocalDateTime now = LocalDateTime.now();
        scheduleRepository.save(
                new SignUpSchedule(1, now.minusDays(2L), now.minusDays(1L), now.plusDays(1L), now.plusDays(2L), now.plusDays(3L)));
    }
}
