package com.inhabas.api.dto.member;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Length(max = 25)
    private String name;

    @NotBlank
    @Length(max = 15)
    private String major;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}")
    private String phoneNumber;

    @NotNull
    private Integer studentId;

    @NotNull
    private Integer grade;

    @NotNull
    private Integer semester;

    @NotNull
    private boolean isProfessor;

    @Builder
    public SignUpForm(String name, String major, String phoneNumber, Integer studentId, Integer grade, Integer semester, boolean isProfessor) {
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.studentId = studentId;
        this.grade = grade;
        this.semester = semester;
        this.isProfessor = isProfessor;
    }
}
