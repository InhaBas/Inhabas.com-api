package com.inhabas.api.auth.domain.oauth2.member.dto;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.security.masking.Masked;
import com.inhabas.api.auth.domain.oauth2.member.security.masking.MaskingType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotApprovedMemberManagementDto {
    @NotBlank
    @Length(max = 50)
    private String name;

    @NotNull
    @Positive
    private Long memberId;

    @NotNull
    private String studentId;

    @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$")
    @Masked(type = MaskingType.PHONE)
    private String phoneNumber;

    @Email
    private String email;

    @NotNull
    private MemberType type;

    @NotNull
    @Positive
    private Integer grade;

    @NotBlank
    @Length(max = 50)
    private String major;

}
