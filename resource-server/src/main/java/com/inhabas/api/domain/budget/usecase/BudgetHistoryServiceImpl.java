package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.HistoryCannotModifiableException;
import com.inhabas.api.domain.budget.NotFoundBudgetHistoryException;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BudgetHistoryServiceImpl implements BudgetHistoryService {

    private final BudgetHistoryRepository repository;

    @Override
    public void createNewHistory(BudgetHistoryCreateForm form, MemberId CFO) {

        BudgetHistory newHistory = form.toEntity(CFO);

        repository.save(newHistory);
    }

    @Override
    public void modifyHistory(BudgetHistoryModifyForm form, MemberId CFO) {

        BudgetHistory budgetHistory = repository.findById(form.getId())
                .orElseThrow(NotFoundBudgetHistoryException::new);

        budgetHistory.modify(
                CFO, form.getIncome(), form.getOutcome(), form.getDateUsed(),
                form.getTitle(), form.getDetails(), form.getPersonReceived());

        repository.save(budgetHistory);
    }


    @Override
    public void deleteHistory(Integer historyId, MemberId CFO) {

        BudgetHistory budgetHistory = repository.findById(historyId)
                .orElseThrow(NotFoundBudgetHistoryException::new);

        if (budgetHistory.cannotModifiableBy(CFO)) {
            throw new HistoryCannotModifiableException();
        }

        repository.deleteById(historyId);
    }


}
