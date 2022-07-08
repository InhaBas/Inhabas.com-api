package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;

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
