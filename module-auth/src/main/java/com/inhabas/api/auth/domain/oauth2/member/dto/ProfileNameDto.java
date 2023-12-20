package com.inhabas.api.auth.domain.oauth2.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class ProfileNameDto {

    @NotBlank
    private String name;

}
