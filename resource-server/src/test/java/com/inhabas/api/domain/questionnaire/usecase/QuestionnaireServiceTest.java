package com.inhabas.api.domain.questionnaire.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import com.inhabas.api.domain.questionnaire.domain.Questionnaire;
import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;
import com.inhabas.api.domain.questionnaire.repository.QuestionnaireRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class QuestionnaireServiceTest {

  @InjectMocks private QuestionnaireServiceImpl questionnaireService;

  @Mock private QuestionnaireRepository questionnaireRepository;

  @DisplayName("db 에 저장되어 있던 질문리스트를 dto 로 반환한다.")
  @Test
  public void getQuestionnaire() {
    // given
    ArrayList<Questionnaire> questionnaireInDatabase =
        new ArrayList<>() {
          {
            add(new Questionnaire(1L, "지원동기 및 목표를 기술해주세요."));
            add(new Questionnaire(2L, "프로그래밍 관련 언어를 다루어 본 적이 있다면 적어주세요."));
            add(new Questionnaire(3L, "빅데이터 관련 활동 혹은 공모전 관련 경험이 있다면 적어주세요."));
            add(new Questionnaire(4L, "추후 희망하는 진로가 무엇이며, 동아리 활동이 진로에 어떠한 영향을 줄 것이라고 생각하나요?"));
            add(new Questionnaire(5L, "어떤 경로로 IBAS를 알게 되셨나요?"));
          }
        };

    given(questionnaireRepository.findAll()).willReturn(questionnaireInDatabase);

    // when
    List<QuestionnaireDto> questionnaireDTOs = questionnaireService.getQuestionnaire();

    // then
    assertThat(questionnaireDTOs)
        .extracting("question")
        .containsExactly(
            "지원동기 및 목표를 기술해주세요.",
            "프로그래밍 관련 언어를 다루어 본 적이 있다면 적어주세요.",
            "빅데이터 관련 활동 혹은 공모전 관련 경험이 있다면 적어주세요.",
            "추후 희망하는 진로가 무엇이며, 동아리 활동이 진로에 어떠한 영향을 줄 것이라고 생각하나요?",
            "어떤 경로로 IBAS를 알게 되셨나요?");
  }
}
