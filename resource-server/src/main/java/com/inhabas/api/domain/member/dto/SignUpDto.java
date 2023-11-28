package com.inhabas.api.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class SignUpDto {

    @NotBlank
    @Length(max = 50)
    private String name;

    @NotBlank
    @Length(max = 50)
    private String major;

    @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$")
    private String phoneNumber;

    @JsonUnwrapped
    @NotNull @Positive
    private StudentId studentId;

    @NotNull
    private MemberType memberType;

    @Builder
    public SignUpDto(String name, String major, String phoneNumber, StudentId studentId, MemberType memberType) {
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.studentId = studentId;
        this.memberType = memberType;
    }
}
