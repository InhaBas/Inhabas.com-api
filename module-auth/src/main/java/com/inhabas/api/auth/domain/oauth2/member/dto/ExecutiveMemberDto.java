package com.inhabas.api.auth.domain.oauth2.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class ExecutiveMemberDto {

  @NotBlank
  @Length(max = 50)
  private String name;

  @NotNull @Positive private Long memberId;

  @NotNull private String studentId;

  @NotBlank private Role role;

  @NotNull private Integer generation;

  @NotBlank
  @Length(max = 50)
  private String major;

  private String picture;

  @Builder
  public ExecutiveMemberDto(
      String name,
      Long memberId,
      String studentId,
      Role role,
      Integer generation,
      String major,
      String picture) {
    this.name = name;
    this.memberId = memberId;
    this.studentId = studentId;
    this.role = role;
    this.generation = generation;
    this.major = major;
    this.picture = picture;
  }
}
