package com.inhabas.api.auth.domain.oauth2.majorInfo.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class MajorInfoSaveDto {

  @NotBlank
  @Length(max = 20)
  private String college;

  @NotBlank
  @Length(max = 50)
  private String major;

  public MajorInfoSaveDto(String college, String major) {
    this.college = college;
    this.major = major;
  }
}
