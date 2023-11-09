package com.inhabas.api.auth.domain.oauth2.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewMemberManagementDto {
    @NotBlank
    @Length(max = 50)
    private String name;

    @NotNull
    @Positive
    private Integer memberId;

    @Pattern(regexp = "\"^(010)-\\\\d{4}-\\\\d{4}$\"")
    private String phoneNumber;

    @Email
    private String email;

    @NotBlank
    @Length(max = 50)
    private String major;

    @Builder
    public NewMemberManagementDto(String name, Integer memberId, String phoneNumber, String email, String major) {
        this.name = name;
        this.memberId = memberId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.major = major;
    }

}
