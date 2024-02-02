package com.inhabas.api.domain.questionnaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.questionnaire.domain.Questionnaire;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

  Long countByIdIn(List<Long> idList);
}
