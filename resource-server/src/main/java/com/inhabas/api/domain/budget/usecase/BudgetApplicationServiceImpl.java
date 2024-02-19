package com.inhabas.api.domain.budget.usecase;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.budget.ApplicationCannotModifiableException;
import com.inhabas.api.domain.budget.ApplicationNotFoundException;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetApplicationServiceImpl implements BudgetApplicationService {

  private final BudgetApplicationRepository repository;

  @Transactional
  @Override
  public void registerApplication(BudgetApplicationRegisterForm form, StudentId applicant) {

    BudgetSupportApplication application = form.toEntity(applicant);
    repository.save(application);
  }

  @Transactional
  @Override
  public void updateApplication(BudgetApplicationUpdateForm form, StudentId applicant) {

    BudgetSupportApplication application =
        repository.findById(form.getApplicationId()).orElseThrow(ApplicationNotFoundException::new);

    application.modify(
        form.getTitle(),
        form.getDateUsed(),
        form.getDetails(),
        form.getOutcome(),
        form.getAccounts(),
        applicant);

    repository.save(application);
  }

  @Transactional
  @Override
  public void deleteApplication(Integer applicationId, StudentId applicant) {

    BudgetSupportApplication application =
        repository.findById(applicationId).orElseThrow(ApplicationNotFoundException::new);

    if (application.cannotModifiableBy(applicant)) {
      throw new ApplicationCannotModifiableException();
    }

    repository.deleteById(applicationId);
  }

  @Override
  public BudgetApplicationDetailDto getApplicationDetails(Integer applicationId) {

    return repository.findDtoById(applicationId).orElseThrow(ApplicationNotFoundException::new);
  }

  @Override
  public Page<BudgetApplicationListDto> getApplications(
      ApplicationStatus status, Pageable pageable) {

    return repository.search(status, pageable);
  }
}
