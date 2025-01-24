package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class BudgetApplicationDto {

  @NotNull private Long id;

  @NotBlank private String title;

  @NotNull private Long applicantId;

  @NotNull private String applicantStudentId;

  @NotBlank private String applicantName;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull private RequestStatus status;

  @Builder
  public BudgetApplicationDto(
      Long id, String title, Member applicant, LocalDateTime dateCreated, RequestStatus status) {
    this.id = id;
    this.title = title;
    this.applicantId = applicant.getId();
    this.applicantStudentId = applicant.getStudentId();
    this.applicantName = applicant.getName();
    this.dateCreated = dateCreated;
    this.status = status;
  }
}
