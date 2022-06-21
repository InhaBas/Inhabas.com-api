package com.inhabas.api.service.signup;

import com.inhabas.api.domain.member.security.LoginMember;
import com.inhabas.api.dto.member.MajorInfoDto;
import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import com.inhabas.api.dto.signUp.SignUpDto;

import java.util.List;

public interface SignUpService {

    void saveSignUpForm(SignUpDto signUpDto, LoginMember authUserDetail);

    SignUpDto loadSignUpForm(LoginMember signUpUser);

    boolean validateFieldsDuplication(MemberDuplicationQueryCondition condition);

    void completeSignUp(LoginMember authUserDetail);

    List<QuestionnaireDto> getQuestionnaire();

    void saveAnswers(List<AnswerDto> answerDtoList, LoginMember authUserDetail);

    List<AnswerDto> getAnswers(LoginMember authUserDetail);

    List<MajorInfoDto> getMajorInfo();

}
