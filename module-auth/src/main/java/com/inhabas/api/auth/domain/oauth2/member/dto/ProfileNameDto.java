package com.inhabas.api.auth.domain.oauth2.member.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ProfileNameDto {

  @NotBlank private String name;

  @Builder
  public ProfileNameDto(String name) {
    this.name = name;
  }
}
