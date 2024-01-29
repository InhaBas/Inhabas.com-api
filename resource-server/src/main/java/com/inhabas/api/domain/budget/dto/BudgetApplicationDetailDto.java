package com.inhabas.api.domain.budget.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetApplicationDetailDto {

  private Integer id;

  private String title;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime dateUsed;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime dateCreated;

  private String details;

  private Integer outcome;

  private String accounts;

  private Integer applicationWriterId;

  private String applicationWriterName;

  private Integer memberIdInCharge;

  private String memberNameInCharge;

  private ApplicationStatus status;

  private String rejectReason;
}
