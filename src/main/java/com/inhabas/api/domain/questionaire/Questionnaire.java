package com.inhabas.api.domain.questionaire;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@Table(name = "question_form")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Questionnaire {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_no")
    private Integer no;

    @Column(name = "quest_name")
    private String item;

    public Questionnaire(Integer no, String item) {
        this.no = no;
        this.item = item;
    }
}
