package com.inhabas.api.domain.policy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PolicyTermDto {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @Builder
    public PolicyTermDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
