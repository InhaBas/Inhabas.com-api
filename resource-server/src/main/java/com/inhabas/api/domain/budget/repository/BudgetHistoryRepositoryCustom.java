package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BudgetHistoryRepositoryCustom {

    Page<BudgetHistoryDetailDto> search(Integer year, Pageable pageable);

    Optional<BudgetHistoryDetailDto> findDtoById(Integer id);
}
