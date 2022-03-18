package com.inhabas.api.dto.signUp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
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

    @NotNull @Positive
    private Integer memberId;

    @Builder
    public SignUpDto(String name, String major, String phoneNumber, String email, Integer memberId) {
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.memberId = memberId;
    }
}
