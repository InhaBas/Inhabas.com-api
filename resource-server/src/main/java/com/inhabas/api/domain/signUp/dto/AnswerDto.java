package com.inhabas.api.domain.signUp.dto;

import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

  @Positive private Long questionId;

  // 길이 검증(1~1000자)은 AnswerServiceImpl 에서 InvalidAnswerLengthException 으로 처리한다.
  private String content;
}
