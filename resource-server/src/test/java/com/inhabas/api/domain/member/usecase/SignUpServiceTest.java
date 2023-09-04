package com.inhabas.api.domain.member.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.inhabas.api.domain.majorInfo.usecase.MajorInfoService;
import com.inhabas.api.domain.member.NoQueryParameterException;
import com.inhabas.api.domain.member.domain.MemberDuplicationCheckerImpl;
import com.inhabas.api.domain.member.domain.MemberServiceImpl;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.questionaire.usecase.QuestionnaireService;
import com.inhabas.api.domain.signUpSchedule.domain.SignUpSchedulerStrict;
import com.inhabas.api.domain.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {

    @InjectMocks
    private SignUpServiceImpl signUpService;

    @Mock
    private MemberServiceImpl memberService;

    @Mock
    private MajorInfoService majorInfoService;

    @Mock
    private AnswerService answerService;

    @Mock
    private QuestionnaireService questionnaireService;

    @Mock
    private MemberDuplicationCheckerImpl memberDuplicationChecker;

    @Mock
    private SignUpSchedulerStrict signUpSchedulerStrict;

    @DisplayName("회원가입폼이 제출되면 저장한다.")
    @Test
    public void saveSignUpFormTest() {
        //given
//        SignUpDto signUpForm = SignUpDto.builder()
//                .name("유동현")
//                .major("컴퓨터공학과")
//                .phoneNumber("010-0000-1111")
//                .email("my@email.com")
//                .memberId(12345678)
//                .memberType(MemberType.UNDERGRADUATE)
//                .build();
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(12345678)
//                .build();
//        doNothing().when(memberService).save(any(Member.class));
//        doNothing().when(authUserService).setProfileIdToSocialAccount(anyInt(), anyInt());
//
//        SignUpScheduleDto signUpScheduleDto = new SignUpScheduleDto();
//        ReflectionTestUtils.setField(signUpScheduleDto, "generation", 1);
//        given(signUpSchedulerStrict.getSchedule()).willReturn(signUpScheduleDto);
//
//        //when
//        signUpService.saveSignUpForm(signUpForm, authUserDetail);
//
//        //then
//        then(memberService).should(times(1)).save(any(Member.class));
//        then(authUserService).should(times(1)).setProfileIdToSocialAccount(anyInt(), anyInt());
    }

    @Disabled
    @DisplayName("임시저장된 회원 프로필 정보를 불러온다.")
    @Test
    public void loadMemberProfileTest() {
        //given
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(12345678)
//                .build();
//        Member storedMember = Member.builder()
//                .id(12345678)
//                .name("유동현")
//                .phoneNumber("010-0000-0000")
//                .email("my@email.com")
//                .picture("")
//                .ibasInformation(new IbasInformation(Role.BASIC_MEMBER))
//                .schoolInformation(SchoolInformation.ofUnderGraduate("전자공학과", 1))
//                .build();
//        given(memberService.findById(anyInt())).willReturn(storedMember);
//
//        //when
//        SignUpDto loadedForm = signUpService.loadSignUpForm(authUserDetail);
//
//        //then
//        SignUpDto expectResult = SignUpDto.builder()
//                .name("유동현")
//                .major("전자공학과")
//                .phoneNumber("010-0000-0000")
//                .email("my@gmail.com")
//                .memberId(12345678)
//                .memberType(MemberType.UNDERGRADUATE)
//                .build();
//
//        assertThat(loadedForm)
//                .usingRecursiveComparison()
//                .isEqualTo(expectResult);
    }

    @Disabled
    @DisplayName("임시저장된 회원 프로필 정보가 없다.")
    @Test
    public void noSavedMemberProfileTest() {
        //given
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(null)
//                .build();
//        given(memberService.findById(any())).willThrow(IllegalArgumentException.class);
//
//        //when
//        SignUpDto loadedForm = signUpService.loadSignUpForm(authUserDetail);
//
//        //then
//        SignUpDto expectResult = SignUpDto.builder()
//                .name(null)
//                .major(null)
//                .phoneNumber(null)
//                .email("my@gmail.com")
//                .memberId(null)
//                .memberType(MemberType.UNDERGRADUATE)
//                .build();
//
//        assertThat(loadedForm)
//                .usingRecursiveComparison()
//                .isEqualTo(expectResult);
    }

    @Disabled
    @DisplayName("소셜 계정 정보에 매핑된 학번으로 회원 데이터를 조회할 수 없으면, 깨끗한 폼을 반환한다.")
    @Test
    public void invalidMappingMemberProfileTest() {
        //given
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(12171652)
//                .build();
//        given(memberService.findById(any())).willThrow(MemberNotFoundException.class);
//
//        //when
//        SignUpDto loadedForm = signUpService.loadSignUpForm(authUserDetail);
//
//        //then
//        SignUpDto expectResult = SignUpDto.builder()
//                .name(null)
//                .major(null)
//                .phoneNumber(null)
//                .email("my@gmail.com")
//                .memberId(null)
//                .memberType(MemberType.UNDERGRADUATE)
//                .build();
//
//        assertThat(loadedForm)
//                .usingRecursiveComparison()
//                .isEqualTo(expectResult);
    }

    @Disabled
    @DisplayName("중복검사를 위한 api 호출 시 파라미터가 하나도 없으면 오류")
    @Test
    public void noParamsForDuplicateValidation() {
        //when
        MemberDuplicationQueryCondition condition = new MemberDuplicationQueryCondition();

        //then
        assertThrows(NoQueryParameterException.class,
                condition::verifyAtLeastOneParameter);
    }

    @Disabled
    @DisplayName("중복 검사 호출")
    @Test
    public void validateForMemberId() {
        //given
        MemberDuplicationQueryCondition condition =
                new MemberDuplicationQueryCondition(new MemberId(12171652), "010-1111-1111");
        given(memberDuplicationChecker.isDuplicatedMember(any(MemberDuplicationQueryCondition.class))).willReturn(true);

        //when
        signUpService.validateFieldsDuplication(condition);

        //then
        then(memberDuplicationChecker).should(times(1)).isDuplicatedMember(any(MemberDuplicationQueryCondition.class));
    }

    @Disabled
    @DisplayName("회원가입 완료 처리")
    @Test
    public void completeSignUpTest() {
        //given
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(12171652)
//                .build();
//        doNothing().when(authUserService).finishSignUp(anyInt());
//        doNothing().when(memberService).changeRole(anyInt(), any());
//        given(memberService.findById(anyInt())).willReturn(Member.builder()
//                .id(12345678)
//                .name("유동현")
//                .phoneNumber("010-0000-0000")
//                .email("my@email.com")
//                .picture("")
//                .ibasInformation(new IbasInformation(Role.BASIC_MEMBER))
//                .schoolInformation(SchoolInformation.ofProfessor("전자공학과", 1))
//                .build());
//
//        //when
//        signUpService.completeSignUp(authUserDetail);
//
//        //then
//        then(authUserService).should(times(1)).finishSignUp(anyInt());
//        then(memberService).should(times(1)).changeRole(anyInt(), any());
    }

    @Disabled
    @DisplayName("답변을 작성하지 않아서 회원가입이 완료되지 않는다.")
    @Test
    public void notYetWriteAnswersTests() {
        //given
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(12171652)
//                .build();
//        given(memberService.findById(anyInt())).willReturn(Member.builder()
//                .id(12345678)
//                .name("유동현")
//                .phoneNumber("010-0000-0000")
//                .email("my@gmail.com")
//                .picture("")
//                .ibasInformation(new IbasInformation(Role.BASIC_MEMBER))
//                .schoolInformation(SchoolInformation.ofUnderGraduate("전자공학과", 1))
//                .build());
//        given(answerService.existAnswersWrittenBy(anyInt())).willReturn(false);
//
//        //then
//        assertThrows(NotWriteAnswersException.class,
//                ()->signUpService.completeSignUp(authUserDetail));
    }

    @Disabled
    @DisplayName("회원정보를 입력하지 않아서 회원가입이 완료되지 않는다.")
    @Test
    public void notYetWriteProfileTests() {
        //given
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(null)
//                .build();
//
//        //then
//        assertThrows(NotWriteProfileException.class,
//                ()->signUpService.completeSignUp(authUserDetail));
    }

    @DisplayName("모든 전공 정보를 불러온다.")
    @Test
    public void getMajorInfoTest() {
        //given
        given(majorInfoService.getAllMajorInfo()).willReturn(List.of(new MajorInfoDto(2, "공대", "전기공학과")));

        //when
        signUpService.getMajorInfo();

        //then
        then(majorInfoService).should(times(1)).getAllMajorInfo();
    }

    @DisplayName("모든 질문지 리스트를 불러온다.")
    @Test
    public void getQuestionnaire() {
        //given
        given(questionnaireService.getQuestionnaire()).willReturn(List.of());

        //when
        signUpService.getQuestionnaire();

        //then
        then(questionnaireService).should(times(1)).getQuestionnaire();
    }

    @Disabled
    @DisplayName("특정 회원이 작성한 답변 리스트를 불러온다.")
    @Test
    public void getAnswers() {
        //given
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(12171652)
//                .build();
//        given(answerService.getAnswers(anyInt())).willReturn(List.of());
//
//        //when
//        signUpService.getAnswers(authUserDetail);
//
//        //then
//        then(answerService).should(times(1)).getAnswers(anyInt());
    }

    @Disabled
    @DisplayName("특정 회원이 작성한 답변을 저장한다.")
    @Test
    public void saveAnswers() {
        //given
//        AuthUserDetail authUserDetail = AuthUserDetail.builder()
//                .id(1)
//                .email("my@gmail.com")
//                .hasJoined(false)
//                .isActive(true)
//                .provider("google")
//                .profileId(12171652)
//                .build();
//        ArrayList<AnswerDto> submittedAnswers = new ArrayList<>() {{
//            add(new AnswerDto(1, "저는 꼭 이 동아리에 입부하고 싶습니다."));
//            add(new AnswerDto(2, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
//            add(new AnswerDto(3, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
//            add(new AnswerDto(4, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
//        }};
//        doNothing().when(answerService).saveAnswers(any(), anyInt());
//
//        //when
//        signUpService.saveAnswers(submittedAnswers, authUserDetail);
//
//        //then
//        then(answerService).should(times(1)).saveAnswers(any(), anyInt());
    }


}
