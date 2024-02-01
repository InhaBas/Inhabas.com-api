package com.inhabas.api.domain.signUp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class QuestionAnswerDto {

    @NotNull
    @Positive
    private Long questionId;

    @NotBlank
    private String question;

    @NotBlank
    private String answer;

    public QuestionAnswerDto(Long questionId, String question, String answer) {
        this.questionId = questionId;
        this.question = question;
        this.answer = answer;
    }

}
