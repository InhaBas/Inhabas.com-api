package com.inhabas.api.auth.domain.oauth2.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileIntroDto {

    private String introduce;
    private Boolean isHOF;

}
