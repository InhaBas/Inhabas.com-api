package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.member.dto.AnswerDto;

import java.util.List;

public interface AnswerService {

    void saveAnswers(List<AnswerDto> submittedAnswers, StudentId studentId);

    List<AnswerDto> getAnswers(StudentId studentId);

    boolean existAnswersWrittenBy(StudentId studentId);
}
