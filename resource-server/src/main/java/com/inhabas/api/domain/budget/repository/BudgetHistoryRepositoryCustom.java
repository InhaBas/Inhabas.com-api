package com.inhabas.api.domain.budget.repository;

import java.util.List;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDto;

public interface BudgetHistoryRepositoryCustom {

  List<BudgetHistoryDto> search(Integer year);

  List<Integer> findAllYear();
}
