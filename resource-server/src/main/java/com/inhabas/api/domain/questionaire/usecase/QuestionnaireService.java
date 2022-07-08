package com.inhabas.api.domain.questionaire.usecase;

import com.inhabas.api.domain.questionaire.dto.QuestionnaireDto;

import java.util.List;

public interface QuestionnaireService {

    List<QuestionnaireDto> getQuestionnaire();
}
