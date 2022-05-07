package com.inhabas.api.service.signup;

import com.inhabas.api.auth.utils.argumentResolver.ResolvedAuthenticationResult;
import com.inhabas.api.domain.member.LoginMember;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberDuplicationChecker;
import com.inhabas.api.domain.member.type.IbasInformation;
import com.inhabas.api.domain.member.type.MemberType;
import com.inhabas.api.domain.member.type.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.member.MajorInfoDto;
import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import com.inhabas.api.dto.signUp.SignUpDto;
import com.inhabas.api.service.member.MajorInfoService;
import com.inhabas.api.service.member.MemberNotFoundException;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.api.service.questionnaire.AnswerService;
import com.inhabas.api.service.questionnaire.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final AnswerService answerService;
    private final MemberService memberService;
    private final MajorInfoService majorInfoService;
    private final QuestionnaireService questionnaireService;
    private final SignUpScheduler signUpScheduler;
    private final MemberDuplicationChecker memberDuplicationChecker;

    private static final MemberType DEFAULT_MEMBER_TYPE = MemberType.UNDERGRADUATE;
    private static final Role DEFAULT_ROLE_BEFORE_FINISH_SIGNUP = Role.ANONYMOUS;


    @Override
    @Transactional
    public void saveSignUpForm(SignUpDto signUpForm, LoginMember authUserDetail) {
        Integer generation = signUpScheduler.getSchedule().getGeneration();
        IbasInformation ibasInformation = new IbasInformation(DEFAULT_ROLE_BEFORE_FINISH_SIGNUP);
        SchoolInformation schoolInformation = new SchoolInformation(signUpForm.getMajor(), generation, signUpForm.getMemberType());

        Member member = Member.builder()
                .id(signUpForm.getMemberId())
                .name(signUpForm.getName())
                .phone(signUpForm.getPhoneNumber())
                .email(authUserDetail.getEmail())
                .ibasInformation(ibasInformation)
                .schoolInformation(schoolInformation)
                .build();

        memberService.save(member);
        throw new NotImplementedException("소셜 계정과 회원을 연결하는 로직 추가해야함");
    }

    @Override
    public void completeSignUp(LoginMember signUpUser) {

        if (notYetWroteProfile(signUpUser)) {
            throw new NotWriteProfileException();
        }
        else if (notYetWroteAnswers(signUpUser)) {
            throw new NotWriteAnswersException();
        }

        memberService.finishSignUp(signUpUser.getMemberId());
    }

    private boolean notYetWroteProfile(ResolvedAuthenticationResult signUpUser) {
        return Objects.isNull(signUpUser.getMemberId());
    }

    private boolean notYetWroteAnswers(ResolvedAuthenticationResult signUpUser) {
        Member member = memberService.findById(signUpUser.getMemberId());

        return member.isUnderGraduate()
                && !answerService.existAnswersWrittenBy(signUpUser.getMemberId());
    }

    @Override
    public List<QuestionnaireDto> getQuestionnaire() {
        return questionnaireService.getQuestionnaire();
    }

    @Override
    public void saveAnswers(List<AnswerDto> answerDtoList, LoginMember authUserDetail) {
        answerService.saveAnswers(answerDtoList, authUserDetail.getMemberId());
    }

    @Override
    public List<AnswerDto> getAnswers(LoginMember authUserDetail) {
        return answerService.getAnswers(authUserDetail.getMemberId());
    }

    @Override
    public List<MajorInfoDto> getMajorInfo() {
        return majorInfoService.getAllMajorInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public SignUpDto loadSignUpForm(LoginMember signUpUser) {

        try {
            Member member = memberService.findById(signUpUser.getMemberId());

            return SignUpDto.builder()
                    .memberId(signUpUser.getMemberId())
                    .phoneNumber(member.getPhone())
                    .email(signUpUser.getEmail())
                    .name(member.getName())
                    .major(member.getSchoolInformation().getMajor())
                    .memberType(member.getSchoolInformation().getMemberType())
                    .build();
        }
        catch (MemberNotFoundException | InvalidDataAccessApiUsageException | IllegalArgumentException e) {
            return SignUpDto.builder()
                    .memberId(null)
                    .phoneNumber(null)
                    .email(signUpUser.getEmail())
                    .name(null)
                    .major(null)
                    .memberType(DEFAULT_MEMBER_TYPE)
                    .build();
        }
    }

    @Override
    public boolean validateFieldsDuplication(MemberDuplicationQueryCondition condition) {

        condition.verityAtLeastOneParameter();

        return memberDuplicationChecker.isDuplicatedMember(condition);
    }


}
