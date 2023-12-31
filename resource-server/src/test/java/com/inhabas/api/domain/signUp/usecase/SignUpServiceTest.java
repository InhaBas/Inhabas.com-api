package com.inhabas.api.domain.signUp.usecase;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.auth.domain.oauth2.majorInfo.usecase.MajorInfoService;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.service.MemberService;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.repository.MemberSocialAccountRepository;
import com.inhabas.api.domain.questionnaire.usecase.QuestionnaireService;
import com.inhabas.api.domain.signUp.exception.NotWriteAnswersException;
import com.inhabas.api.domain.signUp.exception.NotWriteProfileException;
import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.dto.SignUpDto;
import com.inhabas.api.domain.signUpSchedule.usecase.SignUpScheduler;
import com.inhabas.api.domain.signUpSchedule.dto.SignUpScheduleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.signingUpMember1;
import static com.inhabas.api.domain.member.domain.entity.MemberTest.signingUpMemberAfterProfile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {

    @InjectMocks
    private SignUpServiceImpl signUpService;
    @Mock
    private MajorInfoService majorInfoService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberSocialAccountRepository memberSocialAccountRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private SignUpScheduler signUpScheduler;
    @Mock
    private QuestionnaireService questionnaireService;
    @Mock
    private AnswerService answerService;


    @DisplayName("(학생) 개인정보 값이 모두 채워진 상태로 제출되면 저장한다.")
    @Test
    public void isSignedUpTest() {
        //given
        given(memberSocialAccountRepository.existsByMember_Id(any())).willReturn(true);

        //when
        boolean check = signUpService.isSignedUp(any());

        //then
        assertThat(check).isTrue();

    }

    @DisplayName("(학생) 개인정보 값이 모두 채워진 상태로 제출되면 저장한다.")
    @Test
    public void saveSignUpFormTest() {
        //given
        Member member = signingUpMember1();
        SignUpDto signUpForm = SignUpDto.builder()
                .name("유동현")
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .grade(1)
                .studentId("12345678")
                .memberType(MemberType.UNDERGRADUATE)
                .build();

        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        SignUpScheduleDto signUpScheduleDto = new SignUpScheduleDto();
        ReflectionTestUtils.setField(signUpScheduleDto, "generation", 1);
        given(signUpScheduler.getSchedule()).willReturn(signUpScheduleDto);

        //when
        signUpService.saveSignUpForm(signUpForm, member.getId());

        //then
        assertThat(member.getName()).isEqualTo(signUpForm.getName());

    }

    @DisplayName("(학생) 개인정보 값이 덜 채워진 상태로 제출되면 실패한다.")
    @Test
    public void doNotSaveSignUpFormTest() {
        //given
        Member member = signingUpMember1();
        SignUpDto signUpForm = SignUpDto.builder()
                .name("유동현")
                // .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .grade(1)
                .studentId("12345678")
                .memberType(MemberType.UNDERGRADUATE)
                .build();

        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        SignUpScheduleDto signUpScheduleDto = new SignUpScheduleDto();
        ReflectionTestUtils.setField(signUpScheduleDto, "generation", 1);
        given(signUpScheduler.getSchedule()).willReturn(signUpScheduleDto);

        //when
        assertThrows(InvalidInputException.class,
                () -> signUpService.saveSignUpForm(signUpForm, member.getId()));

    }

    @DisplayName("(교수) 개인정보 값이 모두 채워진 상태로 제출되면 회원가입과정이 바로 끝난다.")
    @Test
    public void profSaveSignUpFormTest() {
        //given
        Member member = signingUpMember1();
        SignUpDto signUpForm = SignUpDto.builder()
                .name("유동현")
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .studentId("12345678")
                .memberType(MemberType.PROFESSOR)
                .build();

        SignUpScheduleDto signUpScheduleDto = new SignUpScheduleDto();
        ReflectionTestUtils.setField(signUpScheduleDto, "generation", 1);
        given(signUpScheduler.getSchedule()).willReturn(signUpScheduleDto);
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        //when
        signUpService.saveSignUpForm(signUpForm, member.getId());

        //then
        verify(memberService).finishSignUp(member);

    }

    @DisplayName("임시저장을 하지 않았던 회원 정보는 null 로 반환한다.")
    @Test
    public void noSavedMemberProfileTest() {
        //given
        Member member = signingUpMember1();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        //when
        SignUpDto loadedForm = signUpService.loadSignUpForm(member.getId());

        //then
        SignUpDto expectResult = SignUpDto.builder()
                .name(null)
                .major(null)
                .grade(null)
                .phoneNumber(null)
                .studentId(null)
                .memberType(null)
                .build();

        assertThat(loadedForm)
                .usingRecursiveComparison()
                .isEqualTo(expectResult);
    }

    @DisplayName("임시저장을 수행했던 회원 정보를 불러온다.")
    @Test
    public void loadMemberProfileTest() {
        //given
        Member member = signingUpMemberAfterProfile();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        //when
        SignUpDto loadedForm = signUpService.loadSignUpForm(member.getId());

        //then
        SignUpDto expectResult = SignUpDto.builder()
                .name(member.getName())
                .major(member.getSchoolInformation().getMajor())
                .grade(member.getSchoolInformation().getGrade())
                .phoneNumber(member.getPhone())
                .studentId(member.getStudentId())
                .memberType(member.getSchoolInformation().getMemberType())
                .build();

        assertThat(loadedForm)
                .usingRecursiveComparison()
                .isEqualTo(expectResult);
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

    @DisplayName("답변을 작성하지 않아서 회원가입이 완료되지 않는다.")
    @Test
    public void notYetWriteAnswersTests() {
        //given
        Member member = signingUpMemberAfterProfile();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));


        //then
        assertThrows(NotWriteAnswersException.class,
                ()->signUpService.completeSignUp(new ArrayList<>(), member.getId()));
    }

    @DisplayName("회원정보를 입력하지 않아서 회원가입이 완료되지 않는다.")
    @Test
    public void notYetWriteProfileTests() {
        //given
        Member member = signingUpMember1();

        //when
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        //then
        assertThrows(NotWriteProfileException.class,
                ()->signUpService.completeSignUp(null, member.getId()));

    }

    @DisplayName("특정 회원이 작성한 답변을 저장한다.")
    @Test
    public void saveAnswers() {
        //given
        Member member = signingUpMemberAfterProfile();
        ArrayList<AnswerDto> submittedAnswers = new ArrayList<>() {{
            add(new AnswerDto(1L, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new AnswerDto(2L, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new AnswerDto(3L, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new AnswerDto(4L, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};

        //when
        signUpService.saveAnswers(submittedAnswers, member.getId());

        //then
        then(answerService).should(times(1)).saveAnswers(any(), any());

    }

    @DisplayName("회원가입 완료 처리")
    @Test
    public void completeSignUpTest() {
        //given
        Member member = signingUpMemberAfterProfile();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        ArrayList<AnswerDto> submittedAnswers = new ArrayList<>() {{
            add(new AnswerDto(1L, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new AnswerDto(2L, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new AnswerDto(3L, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new AnswerDto(4L, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};

        //when
        signUpService.completeSignUp(submittedAnswers, member.getId());

        //then
        then(memberService).should(times(1)).finishSignUp(any());
    }

}
