package com.inhabas.api.domain.policy.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SavePolicyTernDto {

  @NotNull private String content;

  @Builder
  public SavePolicyTernDto(String content) {
    this.content = content;
  }
}
