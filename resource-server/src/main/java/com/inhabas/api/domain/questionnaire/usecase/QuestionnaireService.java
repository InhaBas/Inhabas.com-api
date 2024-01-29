package com.inhabas.api.domain.questionnaire.usecase;

import java.util.List;

import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;

public interface QuestionnaireService {

  List<QuestionnaireDto> getQuestionnaire();
}
