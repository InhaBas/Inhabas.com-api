package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BudgetHistoryRepositoryCustom {

    Page<BudgetHistoryDetailDto> findAllByPageable(Pageable pageable);

}
