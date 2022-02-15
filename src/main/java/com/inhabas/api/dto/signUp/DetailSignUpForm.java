package com.inhabas.api.dto.signUp;

import lombok.Builder;
import lombok.Data;

@Data
public class DetailSignUpForm {

    private String name;

    private String major;

    private String phoneNumber;

    private String email;

    private Integer memberId;

    private Integer grade;

    private Integer semester;

    private boolean isProfessor;

    @Builder
    public DetailSignUpForm(String name, String major, String phoneNumber, String email, Integer memberId, Integer grade, Integer semester, boolean isProfessor) {
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.memberId = memberId;
        this.grade = grade;
        this.semester = semester;
        this.isProfessor = isProfessor;
    }
}
