package com.inhabas.api.domain.questionaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireDto {

    private Long id;

    private String question;

}
