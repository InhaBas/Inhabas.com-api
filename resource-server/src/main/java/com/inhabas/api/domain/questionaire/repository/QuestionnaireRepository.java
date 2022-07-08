package com.inhabas.api.domain.questionaire.repository;

import com.inhabas.api.domain.questionaire.domain.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Integer> {
}
