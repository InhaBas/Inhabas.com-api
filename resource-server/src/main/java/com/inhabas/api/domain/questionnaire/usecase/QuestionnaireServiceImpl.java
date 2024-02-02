package com.inhabas.api.domain.questionnaire.usecase;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;
import com.inhabas.api.domain.questionnaire.repository.QuestionnaireRepository;

@Service
@RequiredArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {

  private final QuestionnaireRepository questionnaireRepository;

  @Transactional(readOnly = true)
  public List<QuestionnaireDto> getQuestionnaire() {

    return questionnaireRepository.findAll().stream()
        .map(q -> new QuestionnaireDto(q.getId(), q.getQuestion()))
        .collect(Collectors.toList());
  }
}
