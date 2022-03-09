package com.inhabas.api.dto.signUp;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;

@Data @NoArgsConstructor
public class AnswerDto {

    @Positive
    private Integer questionNo;

    @Length(max = 1000)
    private String answer;

    public AnswerDto(Integer questionNo, String answer) {
        this.questionNo = questionNo;
        this.answer = answer;
    }
}
