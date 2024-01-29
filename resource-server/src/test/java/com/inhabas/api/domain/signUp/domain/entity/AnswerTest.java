package com.inhabas.api.domain.signUp.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.questionnaire.domain.Questionnaire;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswerTest {

  @Test
  @DisplayName("생성자 객체 생성")
  public void Answer() {
    // given
    Member member = MemberTest.getTestBasicMember("12171707");
    Questionnaire questionnaire = new Questionnaire(1L, "hello");
    String content = "Ok... bye";

    // when
    Answer answer = new Answer(member, questionnaire, content);

    // then
    assertThat(member).isEqualTo(answer.getMember());
    assertThat(questionnaire).isEqualTo(answer.getQuestionnaire());
    assertThat(content).isEqualTo(answer.getContent());
  }
}
