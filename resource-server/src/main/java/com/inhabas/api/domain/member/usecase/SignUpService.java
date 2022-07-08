package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.domain.questionaire.dto.QuestionnaireDto;
import com.inhabas.api.domain.member.dto.SignUpDto;

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
