package com.inhabas.api.auth.domain.oauth2.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.security.masking.Masked;
import com.inhabas.api.auth.domain.oauth2.member.security.masking.MaskingType;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovedMemberManagementDto {
  @NotBlank
  @Length(max = 50)
  private String name;

  @NotNull @Positive private Long memberId;

  @NotNull private String studentId;

  @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$")
  @Masked(type = MaskingType.PHONE)
  private String phoneNumber;

  @NotBlank private Role role;

  @NotNull private Integer generation;

  @NotBlank
  @Length(max = 50)
  private String major;
}
