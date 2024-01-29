package com.inhabas.api.domain.budget.dto;

import lombok.Getter;

import org.springframework.data.domain.Page;

@Getter
public class BudgetHistoryListResponse {

  private Page<BudgetHistoryDetailDto> page;

  private Integer balance;

  public BudgetHistoryListResponse(Page<BudgetHistoryDetailDto> page, Integer balance) {
    this.page = page;
    this.balance = balance;
  }
}
