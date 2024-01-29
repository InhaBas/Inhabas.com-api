package com.inhabas.api.domain.signUp.usecase;

import java.util.List;

import com.inhabas.api.domain.signUp.dto.AnswerDto;

public interface AnswerService {

  void saveAnswers(List<AnswerDto> submittedAnswers, Long memberId);

  List<AnswerDto> getAnswers(Long memberId);
}
