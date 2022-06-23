package com.inhabas.api.service.signup;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.domain.member.MemberId;
import com.inhabas.api.dto.member.MajorInfoDto;
import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import com.inhabas.api.dto.signUp.SignUpDto;

import java.util.List;

public interface SignUpService {

    void saveSignUpForm(SignUpDto signUpDto, OAuth2UserInfoAuthentication authentication); // 수정 필요함.

    SignUpDto loadSignUpForm(MemberId memberId, OAuth2UserInfoAuthentication authentication);

    boolean validateFieldsDuplication(MemberDuplicationQueryCondition condition);

    void completeSignUp(MemberId memberId);

    List<QuestionnaireDto> getQuestionnaire();

    void saveAnswers(List<AnswerDto> answerDtoList, MemberId memberId);

    List<AnswerDto> getAnswers(MemberId memberId);

    List<MajorInfoDto> getMajorInfo();

}
