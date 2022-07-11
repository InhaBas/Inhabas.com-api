package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.ApplicationCannotModifiableException;
import com.inhabas.api.domain.budget.ApplicationNotFoundException;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetApplicationServiceImpl implements BudgetApplicationService {

    private final BudgetApplicationRepository repository;


    @Override
    public void registerApplication(BudgetApplicationRegisterForm form, MemberId applicant) {

        BudgetSupportApplication application = form.toEntity(applicant);
        repository.save(application);
    }

    @Override
    public void updateApplication(BudgetApplicationUpdateForm form, MemberId applicant) {

        BudgetSupportApplication application = repository.findById(form.getApplicationId())
                .orElseThrow(ApplicationNotFoundException::new);

        application.modify(
                form.getTitle(), form.getDateUsed(), form.getDetails(), form.getOutcome(), form.getAccounts(), applicant);

        repository.save(application);
    }

    @Override
    public void deleteApplication(Integer applicationId, MemberId applicant) {

        BudgetSupportApplication application = repository.findById(applicationId)
                .orElseThrow(ApplicationNotFoundException::new);

        if (application.cannotModifiableBy(applicant)) {
            throw new ApplicationCannotModifiableException();
        }

        repository.deleteById(applicationId);
    }

    @Override
    public BudgetApplicationDetailDto getApplicationDetails(Integer applicationId) {

        return repository.findDtoById(applicationId)
                .orElseThrow(ApplicationNotFoundException::new);
    }

    @Override
    public Page<BudgetApplicationListDto> getApplications(ApplicationStatus status, Pageable pageable) {

        return repository.search(status, pageable);
    }
}
