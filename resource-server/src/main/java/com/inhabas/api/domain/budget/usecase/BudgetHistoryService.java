package com.inhabas.api.domain.budget.usecase;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;

public interface BudgetHistoryService {

  Long createHistory(BudgetHistoryCreateForm form, List<MultipartFile> files, Long secretaryId);

  void modifyHistory(
      Long historyId, BudgetHistoryCreateForm form, List<MultipartFile> files, Long secretaryId);

  void deleteHistory(Long historyId, Long secretaryId);

  List<BudgetHistoryDetailDto> searchHistoryList(Integer year);

  BudgetHistoryDetailDto getHistory(Long id);

  List<Integer> getAllYearOfHistory();

  Integer getBalance();
}
