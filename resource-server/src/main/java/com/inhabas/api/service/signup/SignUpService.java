package com.inhabas.api.service.signup;

import com.inhabas.api.dto.member.MajorInfoDto;
import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import com.inhabas.api.dto.signUp.SignUpDto;
import com.inhabas.api.security.domain.AuthUserDetail;

import java.util.List;

public interface SignUpService {

    void saveSignUpForm(SignUpDto signUpDto, AuthUserDetail authUserDetail);

    SignUpDto loadSignUpForm(AuthUserDetail signUpUser);

    boolean validateFieldsDuplication(MemberDuplicationQueryCondition condition);

    void completeSignUp(AuthUserDetail authUserDetail);

    List<QuestionnaireDto> getQuestionnaire();

    void saveAnswers(List<AnswerDto> answerDtoList, AuthUserDetail authUserDetail);

    List<AnswerDto> getAnswers(AuthUserDetail authUserDetail);

    List<MajorInfoDto> getMajorInfo();

}
