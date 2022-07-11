package com.inhabas.api.domain.budget.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class BudgetApplicationListDto {

    private Integer applicationId;

    private String title;

    private Integer applicantId;

    private String applicantName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateCreated;

    private ApplicationStatus status;
}
