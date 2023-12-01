package com.inhabas.api.domain.questionaire.repository;

import com.inhabas.api.domain.questionaire.domain.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    Long countByIdIn(List<Long> idList);

}
