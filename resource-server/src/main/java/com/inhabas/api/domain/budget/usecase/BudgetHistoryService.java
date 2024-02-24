package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryListResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BudgetHistoryService {

  Long createNewHistory(BudgetHistoryCreateForm form, Long secretaryId);

  void modifyHistory(Long historyId, BudgetHistoryCreateForm form, Long secretaryId);

  void deleteHistory(Long historyId, Long secretaryId);

  BudgetHistoryListResponse searchHistoryList(Integer year, Pageable pageable);

  BudgetHistoryDetailDto getHistory(Long id);

  List<Integer> getAllYearOfHistory();
}
