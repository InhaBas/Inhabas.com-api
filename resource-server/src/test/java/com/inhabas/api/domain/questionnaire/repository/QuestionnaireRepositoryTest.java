package com.inhabas.api.domain.questionnaire.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.domain.questionnaire.domain.Questionnaire;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
class QuestionnaireRepositoryTest {

  @Autowired private QuestionnaireRepository questionnaireRepository;

  @Test
  @DisplayName("idList와 일치하는것이 몇개 포함되어 있는지 확인한다.")
  @Transactional
  public void countByIdIn() {
    // given
    ArrayList<Questionnaire> questionnaireInDatabase =
        new ArrayList<>() {
          {
            add(new Questionnaire(null, "지원동기 및 목표를 기술해주세요."));
            add(new Questionnaire(null, "프로그래밍 관련 언어를 다루어 본 적이 있다면 적어주세요."));
            add(new Questionnaire(null, "빅데이터 관련 활동 혹은 공모전 관련 경험이 있다면 적어주세요."));
            add(new Questionnaire(null, "추후 희망하는 진로가 무엇이며, 동아리 활동이 진로에 어떠한 영향을 줄 것이라고 생각하나요?"));
            add(new Questionnaire(null, "어떤 경로로 IBAS를 알게 되셨나요?"));
          }
        };
    questionnaireRepository.saveAll(questionnaireInDatabase);

    // when
    List<Long> ids = List.of(1L, 2L, 3L);
    Long shouldBeThree = questionnaireRepository.countByIdIn(ids);

    // then
    assertThat(shouldBeThree).isEqualTo(3);
  }
}
