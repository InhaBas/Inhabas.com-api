package com.inhabas.api.domain.questionnaire.repository;

import com.inhabas.api.domain.questionnaire.domain.Questionnaire;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

  Long countByIdIn(List<Long> idList);
}
