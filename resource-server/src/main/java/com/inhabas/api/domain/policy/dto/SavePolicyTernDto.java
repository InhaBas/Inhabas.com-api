package com.inhabas.api.domain.policy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SavePolicyTernDto {

    @NotNull
    private String content;

    @Builder
    public SavePolicyTernDto(String content) {
        this.content = content;
    }

}
