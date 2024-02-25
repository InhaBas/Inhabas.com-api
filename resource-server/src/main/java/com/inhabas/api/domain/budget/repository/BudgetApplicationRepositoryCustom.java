package com.inhabas.api.domain.budget.repository;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;

public interface BudgetApplicationRepositoryCustom {

  Optional<BudgetApplicationDetailDto> findDtoById(Long applicationId);

  List<BudgetApplicationDto> search(RequestStatus status);
}
