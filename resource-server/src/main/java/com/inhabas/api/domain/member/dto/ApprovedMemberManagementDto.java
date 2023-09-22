package com.inhabas.api.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovedMemberManagementDto {

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
    private Integer generation;

    @NotBlank
    @Length(max = 50)
    private String major;

    @Builder
    public ApprovedMemberManagementDto(String name, Integer memberId, String phoneNumber, String email, Integer generation, String major) {
        this.name = name;
        this.memberId = memberId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.generation = generation;
        this.major = major;
    }

}
