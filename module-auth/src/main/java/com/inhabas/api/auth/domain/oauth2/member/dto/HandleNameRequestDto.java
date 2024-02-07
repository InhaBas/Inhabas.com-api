package com.inhabas.api.auth.domain.oauth2.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HandleNameRequestDto {

  @NotNull private Long id;

  @NotBlank private String status;

  private String rejectReason;

  @Builder
  public HandleNameRequestDto(Long id, String status, String rejectReason) {
    this.id = id;
    this.status = status;
    this.rejectReason = rejectReason;
  }
}
