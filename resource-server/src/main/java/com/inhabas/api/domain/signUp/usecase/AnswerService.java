package com.inhabas.api.domain.signUp.usecase;

import com.inhabas.api.domain.signUp.dto.AnswerDto;

import java.util.List;

public interface AnswerService {

    void saveAnswers(List<AnswerDto> submittedAnswers, Long memberId);

    List<AnswerDto> getAnswers(Long memberId);

}
