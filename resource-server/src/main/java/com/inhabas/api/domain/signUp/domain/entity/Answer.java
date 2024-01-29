package com.inhabas.api.domain.signUp.domain.entity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.questionnaire.domain.Questionnaire;

@Entity
@Table(
    name = "ANSWER",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UNIQUE_USER_ID_QUESTIONNAIRE_ID",
          columnNames = {"USER_ID", "QUESTIONNAIRE_ID"})
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_ANSWER_OF_MEMBER"))
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "QUESTIONNAIRE_ID",
      foreignKey = @ForeignKey(name = "FK_ANSWER_OF_QUESTION_ID"))
  private Questionnaire questionnaire;

  @Column(name = "CONTENT", length = 1000)
  private String content;

  public void setContent(String content) {
    this.content = content;
  }

  public Answer(Member member, Questionnaire questionnaire, String content) {
    this.member = member;
    this.questionnaire = questionnaire;
    this.content = content;
  }
}
