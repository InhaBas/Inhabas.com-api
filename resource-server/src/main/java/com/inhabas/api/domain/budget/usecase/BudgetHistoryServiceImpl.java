package com.inhabas.api.domain.budget.usecase;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.budget.BudgetHistoryNotFoundException;
import com.inhabas.api.domain.budget.HistoryCannotModifiableException;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryListResponse;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetHistoryServiceImpl implements BudgetHistoryService {

  private final BudgetHistoryRepository repository;

  @Override
  @Transactional
  public void createNewHistory(BudgetHistoryCreateForm form, StudentId CFO) {

    BudgetHistory newHistory = form.toEntity(CFO);

    repository.save(newHistory);
  }

  @Override
  @Transactional
  public void modifyHistory(BudgetHistoryModifyForm form, StudentId CFO) {

    BudgetHistory budgetHistory =
        repository.findById(form.getId()).orElseThrow(BudgetHistoryNotFoundException::new);

    budgetHistory.modify(
        CFO,
        form.getIncome(),
        form.getOutcome(),
        form.getDateUsed(),
        form.getTitle(),
        form.getDetails(),
        form.getPersonReceived());

    repository.save(budgetHistory);
  }

  @Override
  @Transactional
  public void deleteHistory(Integer historyId, StudentId CFO) {

    BudgetHistory budgetHistory =
        repository.findById(historyId).orElseThrow(BudgetHistoryNotFoundException::new);

    if (budgetHistory.cannotModifiableBy(CFO)) {
      throw new HistoryCannotModifiableException();
    }

    repository.deleteById(historyId);
  }

  @Override
  public BudgetHistoryListResponse searchHistoryList(Integer year, Pageable pageable) {

    Page<BudgetHistoryDetailDto> listPage = repository.search(year, pageable);
    Integer balance = repository.getBalance();
    return new BudgetHistoryListResponse(listPage, balance);
  }

  @Override
  public BudgetHistoryDetailDto getHistory(Integer id) {
    return repository.findDtoById(id).orElseThrow(BudgetHistoryNotFoundException::new);
  }

  @Override
  public List<Integer> getAllYearOfHistory() {
    return repository.findAllYear();
  }
}
