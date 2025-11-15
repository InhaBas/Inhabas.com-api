package com.inhabas.api.domain.signUp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionAnswerDto {

  @NotNull @Positive private Long questionId;

  @NotBlank private String question;

  @NotBlank private String answer;

  public QuestionAnswerDto(Long questionId, String question, String answer) {
    this.questionId = questionId;
    this.question = question;
    this.answer = answer;
  }
}
