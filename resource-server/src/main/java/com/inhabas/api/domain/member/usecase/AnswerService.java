package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.dto.AnswerDto;

import java.util.List;

public interface AnswerService {

    void saveAnswers(List<AnswerDto> submittedAnswers, MemberId memberId);

    List<AnswerDto> getAnswers(MemberId memberId);

    boolean existAnswersWrittenBy(MemberId memberId);
}
