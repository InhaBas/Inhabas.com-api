package com.inhabas.api.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Email
    private String email;

    @JsonUnwrapped
    @NotNull @Positive
    private StudentId studentId;

    @NotNull
    private MemberType memberType;

    @Builder
    public SignUpDto(String name, String major, String phoneNumber, String email, StudentId studentId, MemberType memberType) {
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.studentId = studentId;
        this.memberType = memberType;
    }
}
