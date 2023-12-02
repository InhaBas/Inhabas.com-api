package com.inhabas.api.domain.signUp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    @Positive
    private Long questionId;

    @Length(max = 1000)
    private String content;

}
