package com.inhabas.api.domain.signUp.usecase;

import java.util.List;

import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;
import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.dto.SignUpDto;

public interface SignUpService {

  void saveSignUpForm(SignUpDto signUpDto, Long memberId);

  SignUpDto loadSignUpForm(Long memberId);

  void completeSignUp(List<AnswerDto> answerDtoList, Long memberId);

  List<QuestionnaireDto> getQuestionnaire();

  void saveAnswers(List<AnswerDto> answerDtoList, Long memberId);

  List<AnswerDto> getAnswers(Long memberId);

  List<MajorInfoDto> getMajorInfo();

  boolean isSignedUp(Long memberId);
}
