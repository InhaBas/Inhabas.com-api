package com.inhabas.api.domain.budget.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;

public interface BudgetApplicationRepositoryCustom {

  Optional<BudgetApplicationDetailDto> findDtoById(Integer applicationId);

  Page<BudgetApplicationListDto> search(ApplicationStatus status, Pageable pageable);
}
