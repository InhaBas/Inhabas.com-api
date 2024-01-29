package com.inhabas.api.domain.questionnaire.usecase;

import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;
import java.util.List;

public interface QuestionnaireService {

  List<QuestionnaireDto> getQuestionnaire();
}
