package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.domain.BudgetHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetHistoryRepository extends JpaRepository<BudgetHistory, Integer>, BudgetHistoryRepositoryCustom {

}
