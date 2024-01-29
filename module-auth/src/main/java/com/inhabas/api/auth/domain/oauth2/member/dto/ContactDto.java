package com.inhabas.api.auth.domain.oauth2.member.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactDto {

  @NotBlank private String name;

  @NotBlank private String phoneNumber;

  @NotBlank private String email;

  public ContactDto(String name, String phoneNumber, String email) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.email = email;
  }
}
