package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BudgetApplicationService {

    void registerApplication(BudgetApplicationRegisterForm form, StudentId applicant);

    void updateApplication(BudgetApplicationUpdateForm form, StudentId applicant);

    void deleteApplication(Integer applicationId, StudentId applicant);

    BudgetApplicationDetailDto getApplicationDetails(Integer applicationId);

    Page<BudgetApplicationListDto> getApplications(ApplicationStatus status, Pageable pageable);
}
