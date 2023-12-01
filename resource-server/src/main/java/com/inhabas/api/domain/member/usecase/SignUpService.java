package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.dto.SignUpDto;
import com.inhabas.api.domain.questionaire.dto.QuestionnaireDto;

import java.util.List;

public interface SignUpService {

    void saveSignUpForm(SignUpDto signUpDto, Long memberId);

    SignUpDto loadSignUpForm(Long memberId);

    void completeSignUp(List<AnswerDto> answerDtoList, Long memberId);

    List<QuestionnaireDto> getQuestionnaire();

    void saveAnswers(List<AnswerDto> answerDtoList, Long memberId);

    List<AnswerDto> getAnswers(Long memberId);

    List<MajorInfoDto> getMajorInfo();

}
