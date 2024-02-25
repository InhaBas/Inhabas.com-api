package com.inhabas.api.domain.budget.usecase;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;

@Service
@RequiredArgsConstructor
public class BudgetApplicationProcessorImpl implements BudgetApplicationProcessor {

  private final BudgetApplicationRepository applicationRepository;
  private final BudgetHistoryRepository historyRepository;
  private final MemberRepository memberRepository;

  @PreAuthorize("hasRole('SECRETARY')")
  @Transactional
  @Override
  public void process(
      Long applicationId, BudgetApplicationStatusChangeRequest request, Long memberId) {

    Member inCharge = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    BudgetSupportApplication application =
        applicationRepository.findById(applicationId).orElseThrow(NotFoundException::new);

    switch (request.getStatus()) {
      case PENDING:
        application.pending(inCharge);
        break;

      case APPROVED:
        application.approve(inCharge);
        break;

      case REJECTED:
        application.reject(request.getRejectReason(), inCharge);
        break;

      case COMPLETED:
        application.complete(inCharge);
        historyRepository.save(application.makeHistory()); // application to history
        applicationRepository.save(application);
        break;
    }
  }
}
