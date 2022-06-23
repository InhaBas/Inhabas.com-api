package com.inhabas.api.dto.signUp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.member.MemberId;
import com.inhabas.api.domain.member.type.MemberType;
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
    @Length(max = 25)
    private String name;

    @NotBlank
    @Length(max = 15)
    private String major;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}")
    private String phoneNumber;

    @Email
    private String email;

    @JsonUnwrapped
    @NotNull @Positive
    private MemberId memberId;

    @NotNull
    private MemberType memberType;

    @Builder
    public SignUpDto(String name, String major, String phoneNumber, String email, MemberId memberId, MemberType memberType) {
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.memberId = memberId;
        this.memberType = memberType;
    }
}
