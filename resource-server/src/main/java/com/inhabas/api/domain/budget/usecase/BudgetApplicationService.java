package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BudgetApplicationService {

    void registerApplication(BudgetApplicationRegisterForm form, MemberId applicant);

    void updateApplication(BudgetApplicationUpdateForm form, MemberId applicant);

    void deleteApplication(Integer applicationId, MemberId applicant);

    BudgetApplicationDetailDto getApplicationDetails(Integer applicationId);

    Page<BudgetApplicationListDto> getApplications(ApplicationStatus status, Pageable pageable);
}
