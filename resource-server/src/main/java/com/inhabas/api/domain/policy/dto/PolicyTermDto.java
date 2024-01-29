package com.inhabas.api.domain.policy.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PolicyTermDto {

  @NotNull private String title;

  @NotNull private String content;

  @Builder
  public PolicyTermDto(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
