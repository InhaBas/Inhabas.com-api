package com.inhabas.api.domain.budget.dto;

import lombok.Getter;

import com.inhabas.api.global.dto.PagedResponseDto;

@Getter
public class BudgetHistoryListResponse {

  private PagedResponseDto<BudgetHistoryDetailDto> page;

  private Integer balance;

  public BudgetHistoryListResponse(PagedResponseDto<BudgetHistoryDetailDto> page, Integer balance) {
    this.page = page;
    this.balance = balance;
  }
}
