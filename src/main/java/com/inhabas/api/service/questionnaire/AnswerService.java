package com.inhabas.api.service.questionnaire;

import com.inhabas.api.domain.questionaire.Answer;
import com.inhabas.api.dto.signUp.AnswerDto;

import java.util.List;

public interface AnswerService {

    List<Answer> saveAnswers(List<AnswerDto> submittedAnswers, Integer memberId);

    List<AnswerDto> getAnswers(Integer memberId);
}
