package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.domain.member.dto.AnswerDto;

import java.util.List;

public interface AnswerService {

    void saveAnswers(List<AnswerDto> submittedAnswers, Long memberId);

    List<AnswerDto> getAnswers(Long memberId);

}
