package com.inhabas.api.auth.domain.oauth2.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class ApprovedMemberSummaryDto {
  @NotBlank
  @Length(max = 50)
  private String name;

  @NotNull private String studentId;

  @NotNull private MemberType memberType;

  @NotNull private Integer generation;

  @NotBlank
  @Length(max = 50)
  private String major;

  @Builder
  public ApprovedMemberSummaryDto(
      String name, String studentId, MemberType memberType, Integer generation, String major) {
    this.name = name;
    this.studentId = studentId;
    this.memberType = memberType;
    this.generation = generation;
    this.major = major;
  }
}
