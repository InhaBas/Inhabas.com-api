package com.inhabas.api.service.questionnaire;

import com.inhabas.api.dto.signUp.AnswerDto;

import java.util.List;

public interface AnswerService {

    void saveAnswers(List<AnswerDto> submittedAnswers, Integer memberId);

    List<AnswerDto> getAnswers(Integer memberId);

    boolean existAnswersWrittenBy(Integer memberId);
}
