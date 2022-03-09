package com.inhabas.api.service.questionnaire;

import com.inhabas.api.dto.signUp.QuestionnaireDto;

import java.util.List;

public interface QuestionnaireService {

    List<QuestionnaireDto> getQuestionnaire();
}
