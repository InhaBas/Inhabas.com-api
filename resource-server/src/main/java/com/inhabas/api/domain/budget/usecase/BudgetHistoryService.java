package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryListResponse;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BudgetHistoryService {

  void createNewHistory(BudgetHistoryCreateForm form, StudentId CFO);

  void modifyHistory(BudgetHistoryModifyForm historyId, StudentId CFO);

  void deleteHistory(Integer historyId, StudentId CFO);

  BudgetHistoryListResponse searchHistoryList(Integer year, Pageable pageable);

  BudgetHistoryDetailDto getHistory(Integer id);

  List<Integer> getAllYearOfHistory();
}
