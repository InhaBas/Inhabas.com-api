package com.inhabas.api.domain.signUp.dto;

import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

  @Positive private Long questionId;

  @Length(max = 1000)
  private String content;
}
