package com.inhabas.api.domain.budget.usecase;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.exception.ApplicationCannotModifiableException;
import com.inhabas.api.domain.budget.exception.ApplicationNotFoundException;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;

@Service
@RequiredArgsConstructor
public class BudgetApplicationServiceImpl implements BudgetApplicationService {

  private final BudgetApplicationRepository budgetApplicationRepository;
  private final MemberRepository memberRepository;

  @Override
  public List<BudgetApplicationDto> getApplications(RequestStatus status) {
    return budgetApplicationRepository.search(status);
  }

  @Override
  public BudgetApplicationDetailDto getApplicationDetails(Long applicationId) {

    return budgetApplicationRepository
        .findDtoById(applicationId)
        .orElseThrow(NotFoundException::new);
  }

  @Transactional
  @Override
  public Long registerApplication(BudgetApplicationRegisterForm form, Long memberId) {
    Member applicant =
        memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    BudgetSupportApplication application = form.toEntity(applicant);
    return budgetApplicationRepository.save(application).getId();
  }

  @Transactional
  @Override
  public void updateApplication(
      Long applicationId, BudgetApplicationRegisterForm form, Long memberId) {
    Member applicant =
        memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    BudgetSupportApplication application =
        budgetApplicationRepository.findById(applicationId).orElseThrow(NotFoundException::new);

    application.modify(
        form.getTitle(),
        form.getDateUsed(),
        form.getDetails(),
        form.getOutcome(),
        form.getAccounts(),
        applicant);

    budgetApplicationRepository.save(application);
  }

  @Transactional
  @Override
  public void deleteApplication(Long applicationId, Long memberId) {
    Member applicant =
        memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    BudgetSupportApplication application =
        budgetApplicationRepository
            .findById(applicationId)
            .orElseThrow(ApplicationNotFoundException::new);

    if (application.cannotModifiableBy(applicant)) {
      throw new ApplicationCannotModifiableException();
    }

    budgetApplicationRepository.deleteById(applicationId);
  }
}
