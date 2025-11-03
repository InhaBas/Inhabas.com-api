package com.inhabas.api.domain.signUp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class SignUpDto {

  @NotBlank
  @Length(max = 50)
  @Schema(example = "조승현")
  private String name;

  @NotBlank
  @Length(max = 50)
  @Schema(example = "컴퓨터공학과")
  private String major;

  @Pattern(regexp = "^(010)-\\d{4}-\\d{4}$")
  @Schema(example = "010-0101-0101")
  private String phoneNumber;

  @NotNull
  @Pattern(regexp = "\\d+")
  @Schema(example = "12171707")
  private String studentId;

  @NotNull
  @Schema(
      example = "UNDERGRADUATE",
      allowableValues = {"UNDERGRADUATE", "BACHELOR", "GRADUATED", "PROFESSOR", "OTHER"})
  private MemberType memberType;

  @Positive
  @Schema(example = "1")
  private Integer grade;

  @Builder
  public SignUpDto(
      String name,
      String major,
      String phoneNumber,
      String studentId,
      MemberType memberType,
      Integer grade) {
    this.name = name;
    this.major = major;
    this.phoneNumber = phoneNumber;
    this.studentId = studentId;
    this.memberType = memberType;
    this.grade = grade;
  }
}
