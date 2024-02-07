package com.inhabas.api.domain.signUp.usecase;

import java.util.List;

import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.dto.ApplicationDetailDto;

public interface AnswerService {

  void saveAnswers(List<AnswerDto> submittedAnswers, Long memberId);

  List<AnswerDto> getAnswers(Long memberId);

  ApplicationDetailDto getApplication(Long memberId);
}
