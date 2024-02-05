package com.inhabas.api.auth.domain.oauth2.member.dto;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
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
  private MemberType type;

  @Builder
  public ProfileDetailDto(String major, Integer grade, String phoneNumber, MemberType type) {
    this.major = major;
    this.grade = grade;
    this.phoneNumber = phoneNumber;
    this.type = type;
  }
}
