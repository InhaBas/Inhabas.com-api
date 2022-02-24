package com.inhabas.api.dto.signUp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionnaireDto {

    private Integer id;

    private String question;

    public QuestionnaireDto(Integer id, String question) {
        this.id = id;
        this.question = question;
    }
}
