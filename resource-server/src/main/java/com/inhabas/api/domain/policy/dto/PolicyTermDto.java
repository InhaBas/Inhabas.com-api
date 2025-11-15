package com.inhabas.api.domain.policy.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PolicyTermDto {

  @NotNull private Long id;

  @NotNull private Long policyTypeId;

  @NotNull private String title;

  @NotNull private String content;

  @Builder
  public PolicyTermDto(Long id, Long policyTypeId, String title, String content) {
    this.id = id;
    this.policyTypeId = policyTypeId;
    this.title = title;
    this.content = content;
  }
}
