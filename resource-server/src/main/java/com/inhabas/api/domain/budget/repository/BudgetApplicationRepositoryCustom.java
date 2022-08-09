package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BudgetApplicationRepositoryCustom {

    Optional<BudgetApplicationDetailDto> findDtoById(Integer applicationId);

    Page<BudgetApplicationListDto> search(ApplicationStatus status, Pageable pageable);
}
