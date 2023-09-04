package com.inhabas.api.domain.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;

@Data @NoArgsConstructor
public class AnswerDto {

    @Positive
    private Integer questionNo;

    @Length(max = 1000)
    private String content;

    public AnswerDto(Integer questionNo, String content) {
        this.questionNo = questionNo;
        this.content = content;
    }
}
