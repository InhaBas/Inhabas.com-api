package com.inhabas.api.service.signup;

import com.inhabas.api.domain.member.*;
import com.inhabas.api.domain.member.type.MemberType;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.member.MajorInfoDto;
import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import com.inhabas.api.dto.signUp.SignUpDto;
import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.security.domain.AuthUserService;
import com.inhabas.api.service.member.MajorInfoService;
import com.inhabas.api.service.member.MemberNotFoundException;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.api.service.questionnaire.AnswerService;
import com.inhabas.api.service.questionnaire.QuestionnaireService;
import lombok.RequiredArgsConstructor;
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
    private final MemberDuplicationChecker memberDuplicationChecker;
    private final AuthUserService authUserService;

    private static final MemberType DEFAULT_MEMBER_TYPE = MemberType.UNDERGRADUATE;
    private static final Role DEFAULT_ROLE_AFTER_FINISH_SIGNUP = Role.NOT_APPROVED_MEMBER;
    private static final Role DEFAULT_ROLE_BEFORE_FINISH_SIGNUP = Role.ANONYMOUS;


    @Override
    @Transactional
    public void saveSignUpForm(SignUpDto signUpForm, AuthUserDetail authUserDetail) {

        IbasInformation ibasInformation = new IbasInformation(DEFAULT_ROLE_BEFORE_FINISH_SIGNUP);
        SchoolInformation schoolInformation = SchoolInformation.ofUnderGraduate(signUpForm.getMajor(), 1);  // 변경해야함.

        Member member = Member.builder()
                .id(signUpForm.getMemberId())
                .name(signUpForm.getName())
                .phone(signUpForm.getPhoneNumber())
                .ibasInformation(ibasInformation)
                .schoolInformation(schoolInformation)
                .build();

        memberService.save(member);
        authUserService.setProfileIdToSocialAccount(authUserDetail.getId(), signUpForm.getMemberId());
    }

    @Override
    public void completeSignUp(AuthUserDetail signUpUser) {

        if (notYetWroteProfile(signUpUser)) {
            throw new NotWriteProfileException();
        }
        else if (notYetWroteAnswers(signUpUser)) {
            throw new NotWriteAnswersException();
        }

        authUserService.finishSignUp(signUpUser.getId());
        memberService.changeRole(signUpUser.getProfileId(), DEFAULT_ROLE_AFTER_FINISH_SIGNUP);
    }

    private boolean notYetWroteProfile(AuthUserDetail signUpUser) {
        return Objects.isNull(signUpUser.getProfileId());
    }

    private boolean notYetWroteAnswers(AuthUserDetail signUpUser) {
        Member member = memberService.findById(signUpUser.getProfileId());

        return member.isUnderGraduate()
                && !answerService.existAnswersWrittenBy(signUpUser.getProfileId());
    }

    @Override
    public List<QuestionnaireDto> getQuestionnaire() {
        return questionnaireService.getQuestionnaire();
    }

    @Override
    public void saveAnswers(List<AnswerDto> answerDtoList, AuthUserDetail authUserDetail) {
        answerService.saveAnswers(answerDtoList, authUserDetail.getProfileId());
    }

    @Override
    public List<AnswerDto> getAnswers(AuthUserDetail authUserDetail) {
        return answerService.getAnswers(authUserDetail.getProfileId());
    }

    @Override
    public List<MajorInfoDto> getMajorInfo() {
        return majorInfoService.getAllMajorInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public SignUpDto loadSignUpForm(AuthUserDetail signUpUser) {

        try {
            Member member = memberService.findById(signUpUser.getProfileId());

            return SignUpDto.builder()
                    .memberId(signUpUser.getProfileId())
                    .phoneNumber(member.getPhone())
                    .email(signUpUser.getEmail())
                    .name(member.getName())
                    .major(member.getSchoolInformation().getMajor())
                    .memberType(member.getSchoolInformation().getMemberType())
                    .build();
        }
        catch (MemberNotFoundException | IllegalArgumentException e) {
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
