package com.inhabas.api.domain.budget.repository;

import java.util.List;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;

public interface BudgetApplicationRepositoryCustom {

  List<BudgetApplicationDto> search(RequestStatus status);
}
