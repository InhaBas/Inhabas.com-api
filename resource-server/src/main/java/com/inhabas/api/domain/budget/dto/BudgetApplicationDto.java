package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;

@Getter
@AllArgsConstructor
public class BudgetApplicationDto {

  private Integer applicationId;

  private String title;

  private Integer applicantId;

  private String applicantName;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime dateCreated;

  private ApplicationStatus status;
}
