package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.dto.SignUpDto;
import com.inhabas.api.domain.questionaire.dto.QuestionnaireDto;

import java.util.List;

public interface SignUpService {

    void saveSignUpForm(SignUpDto signUpDto, OAuth2UserInfoAuthentication authentication); // 수정 필요함.

    SignUpDto loadSignUpForm(StudentId studentId, OAuth2UserInfoAuthentication authentication);

    boolean validateFieldsDuplication(MemberDuplicationQueryCondition condition);

    void completeSignUp(StudentId studentId);

    List<QuestionnaireDto> getQuestionnaire();

    void saveAnswers(List<AnswerDto> answerDtoList, StudentId studentId);

    List<AnswerDto> getAnswers(StudentId studentId);

    List<MajorInfoDto> getMajorInfo();

}
