package com.inhabas.api.domain.myInfo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class MyBudgetSupportApplicationDto {

  @NotNull private Long id;

  @NotNull private RequestStatus status;

  @NotBlank private String title;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateChecked;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateDeposited;

  @Builder
  public MyBudgetSupportApplicationDto(
      Long id,
      RequestStatus status,
      String title,
      LocalDateTime dateCreated,
      LocalDateTime dateChecked,
      LocalDateTime dateDeposited) {
    this.id = id;
    this.status = status;
    this.title = title;
    this.dateCreated = dateCreated;
    this.dateChecked = dateChecked;
    this.dateDeposited = dateDeposited;
  }
}
