package com.inhabas.api.domain.budget.repository;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;

public interface BudgetHistoryRepositoryCustom {

  List<BudgetHistoryDetailDto> search(Integer year);

  Optional<BudgetHistoryDetailDto> findDtoById(Long id);

  List<Integer> findAllYear();
}
