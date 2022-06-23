package com.inhabas.api.service.questionnaire;

import com.inhabas.api.domain.member.MemberId;
import com.inhabas.api.dto.signUp.AnswerDto;

import java.util.List;

public interface AnswerService {

    void saveAnswers(List<AnswerDto> submittedAnswers, MemberId memberId);

    List<AnswerDto> getAnswers(MemberId memberId);

    boolean existAnswersWrittenBy(MemberId memberId);
}
