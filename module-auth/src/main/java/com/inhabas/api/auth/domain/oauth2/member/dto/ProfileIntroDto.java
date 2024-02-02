package com.inhabas.api.auth.domain.oauth2.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileIntroDto {

  private String introduce;
  private Boolean isHOF;

  @Builder
  public ProfileIntroDto(String introduce, Boolean isHOF) {
    this.introduce = introduce;
    this.isHOF = isHOF;
  }
}
