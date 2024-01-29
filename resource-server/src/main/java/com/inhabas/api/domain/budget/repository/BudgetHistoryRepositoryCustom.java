package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BudgetHistoryRepositoryCustom {

  Page<BudgetHistoryDetailDto> search(Integer year, Pageable pageable);

  Optional<BudgetHistoryDetailDto> findDtoById(Integer id);

  List<Integer> findAllYear();
}
