package com.inhabas.api.domain.budget.usecase;

import java.util.List;

import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDto;

public interface BudgetHistoryService {

  Long createHistory(BudgetHistoryCreateForm form, Long secretaryId);

  void modifyHistory(Long historyId, BudgetHistoryCreateForm form, Long secretaryId);

  void deleteHistory(Long historyId, Long secretaryId);

  List<BudgetHistoryDto> searchHistoryList(Integer year);

  BudgetHistoryDetailDto getHistory(Long id);

  List<Integer> getAllYearOfHistory();

  Integer getBalance();
}
