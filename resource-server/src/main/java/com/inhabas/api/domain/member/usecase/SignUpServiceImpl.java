package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.auth.domain.oauth2.majorInfo.usecase.MajorInfoService;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.service.MemberDuplicationChecker;
import com.inhabas.api.auth.domain.oauth2.member.domain.service.MemberService;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.*;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;


import com.inhabas.api.domain.member.domain.exception.NotWriteAnswersException;
import com.inhabas.api.domain.member.domain.exception.NotWriteProfileException;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.dto.SignUpDto;
import com.inhabas.api.domain.questionaire.dto.QuestionnaireDto;
import com.inhabas.api.domain.questionaire.usecase.QuestionnaireService;
import com.inhabas.api.domain.signUpSchedule.domain.SignUpScheduler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.UNDERGRADUATE;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.ANONYMOUS;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final AnswerService answerService;
    private final MemberService memberService;
    private final MajorInfoService majorInfoService;
    private final QuestionnaireService questionnaireService;
    private final SignUpScheduler signUpScheduler;
    private final MemberDuplicationChecker memberDuplicationChecker;

    private static final MemberType DEFAULT_MEMBER_TYPE = UNDERGRADUATE;
    private static final Role DEFAULT_ROLE_BEFORE_FINISH_SIGNUP = ANONYMOUS;


    @Override
    @Transactional
    public void saveSignUpForm(SignUpDto signUpForm, OAuth2UserInfoAuthentication authentication) {
        Integer generation = signUpScheduler.getSchedule().getGeneration();
        IbasInformation ibasInformation = new IbasInformation(DEFAULT_ROLE_BEFORE_FINISH_SIGNUP);
        SchoolInformation schoolInformation = new SchoolInformation(signUpForm.getMajor(), generation, signUpForm.getMemberType());

        Member member = Member.builder()
                .studentId(signUpForm.getStudentId())
                .name(signUpForm.getName())
                .phone(signUpForm.getPhoneNumber())
                .email(authentication.getEmail())
                .ibasInformation(ibasInformation)
                .schoolInformation(schoolInformation)
                .build();

        memberService.save(member);
        throw new NotImplementedException("소셜 계정과 회원을 연결하는 로직 추가해야함");
    }

    @Override
    public void completeSignUp(StudentId studentId) {

        Member member = memberService.findById(studentId);

        if (notYetWroteProfile(member)) {
            throw new NotWriteProfileException();
        }
        else if (notYetWroteAnswers(member)) {
            throw new NotWriteAnswersException();
        }

        memberService.finishSignUp(member);
    }

    private boolean notYetWroteProfile(Member member) {
        return Objects.isNull(member);
    }

    private boolean notYetWroteAnswers(Member member) {
        return member.isUnderGraduate()
                && !answerService.existAnswersWrittenBy(member.getStudentId());
    }

    @Override
    public List<QuestionnaireDto> getQuestionnaire() {
        return questionnaireService.getQuestionnaire();
    }

    @Override
    public void saveAnswers(List<AnswerDto> answerDtoList, StudentId studentId) {
        answerService.saveAnswers(answerDtoList, studentId);
    }

    @Override
    public List<AnswerDto> getAnswers(Long memberId) {
        return answerService.getAnswers(memberId);
    }

    @Override
    public List<MajorInfoDto> getMajorInfo() {
        return majorInfoService.getAllMajorInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public SignUpDto loadSignUpForm(StudentId studentId, OAuth2UserInfoAuthentication authentication) {

        try {
            Member member = memberService.findById(studentId);

            return SignUpDto.builder()
                    .studentId(studentId)
                    .phoneNumber(member.getPhone())
                    .email(member.getEmail())
                    .name(member.getName())
                    .major(member.getSchoolInformation().getMajor())
                    .memberType(member.getSchoolInformation().getMemberType())
                    .build();
        }
        catch (MemberNotFoundException | InvalidDataAccessApiUsageException | IllegalArgumentException e) {
            return SignUpDto.builder()
                    .studentId(null)
                    .phoneNumber(null)
                    .email(authentication.getEmail())
                    .name(null)
                    .major(null)
                    .memberType(DEFAULT_MEMBER_TYPE)
                    .build();
        }
    }

    @Override
    public boolean validateFieldsDuplication(MemberDuplicationQueryCondition condition) {

        condition.verifyTwoParameters();

        return memberDuplicationChecker.isDuplicatedMember(condition);
    }


}
