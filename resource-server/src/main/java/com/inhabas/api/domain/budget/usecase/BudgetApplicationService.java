package com.inhabas.api.domain.budget.usecase;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;

public interface BudgetApplicationService {

  Long registerApplication(
      BudgetApplicationRegisterForm form, List<MultipartFile> files, Long memberId);

  void updateApplication(Long applicationId, BudgetApplicationRegisterForm form, Long memberId);

  void deleteApplication(Long applicationId, Long memberId);

  BudgetApplicationDetailDto getApplicationDetails(Long applicationId);

  List<BudgetApplicationDto> getApplications(RequestStatus status);
}
