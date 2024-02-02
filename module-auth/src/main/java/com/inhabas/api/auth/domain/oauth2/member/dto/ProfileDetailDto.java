package com.inhabas.api.auth.domain.oauth2.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileDetailDto {

  private String major;
  private Integer grade;
  private String phoneNumber;

  @Builder
  public ProfileDetailDto(String major, Integer grade, String phoneNumber) {
    this.major = major;
    this.grade = grade;
    this.phoneNumber = phoneNumber;
  }
}
