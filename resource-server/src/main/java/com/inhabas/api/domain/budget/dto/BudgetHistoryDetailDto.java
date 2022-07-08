package com.inhabas.api.domain.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BudgetHistoryDetailDto {

    private LocalDateTime dateUsed;

    private LocalDateTime dateCreated;

    private LocalDateTime dateModified;

    private String title;

    private Integer income;

    private Integer outcome;

    private String details;

    private Integer receivedMemberId;

    private String receivedMemberName;

    private Integer memberIdInCharge;

    private String memberNameInCharge;
}
