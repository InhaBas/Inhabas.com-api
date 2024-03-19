package com.inhabas.api.domain.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;

public interface BudgetApplicationRepository
    extends JpaRepository<BudgetSupportApplication, Long>, BudgetApplicationRepositoryCustom {}
