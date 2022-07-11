package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetApplicationRepository
        extends JpaRepository<BudgetSupportApplication, Integer>, BudgetApplicationRepositoryCustom {
}
