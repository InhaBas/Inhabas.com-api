package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.domain.BudgetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BudgetHistoryRepository
    extends JpaRepository<BudgetHistory, Integer>, BudgetHistoryRepositoryCustom {

  @Query("SELECT sum(e.income.value)-sum(e.outcome.value) from BudgetHistory e")
  Integer getBalance();
}
