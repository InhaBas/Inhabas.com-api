package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.budget.ApplicationNotFoundException;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetApplicationProcessorImpl implements BudgetApplicationProcessor {

  private final BudgetApplicationRepository applicationRepository;
  private final BudgetHistoryRepository historyRepository;

  @PreAuthorize("hasRole('SECRETARY')")
  @Transactional
  @Override
  public void process(
      Integer applicationId, BudgetApplicationStatusChangeRequest request, StudentId inCharge) {

    BudgetSupportApplication application =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(ApplicationNotFoundException::new);

    switch (request.getStatus()) {
      case WAITING:
        application.waiting(inCharge);
        break;

      case APPROVED:
        application.approve(inCharge);
        break;

      case DENIED:
        application.deny(request.getRejectReason(), inCharge);
        break;

      case PROCESSED:
        application.process(inCharge);
        historyRepository.save(application.makeHistory()); // application to history
        applicationRepository.save(application);
        break;
    }
  }
}
