package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.HistoryCannotModifiableException;
import com.inhabas.api.domain.budget.NotFoundBudgetHistoryException;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetHistoryServiceImpl implements BudgetHistoryService {

    private final BudgetHistoryRepository repository;

    @Override
    @Transactional
    public void createNewHistory(BudgetHistoryCreateForm form, MemberId CFO) {

        BudgetHistory newHistory = form.toEntity(CFO);

        repository.save(newHistory);
    }

    @Override
    @Transactional
    public void modifyHistory(BudgetHistoryModifyForm form, MemberId CFO) {

        BudgetHistory budgetHistory = repository.findById(form.getId())
                .orElseThrow(NotFoundBudgetHistoryException::new);

        budgetHistory.modify(
                CFO, form.getIncome(), form.getOutcome(), form.getDateUsed(),
                form.getTitle(), form.getDetails(), form.getPersonReceived());

        repository.save(budgetHistory);
    }


    @Override
    @Transactional
    public void deleteHistory(Integer historyId, MemberId CFO) {

        BudgetHistory budgetHistory = repository.findById(historyId)
                .orElseThrow(NotFoundBudgetHistoryException::new);

        if (budgetHistory.cannotModifiableBy(CFO)) {
            throw new HistoryCannotModifiableException();
        }

        repository.deleteById(historyId);
    }

    @Override
    public Page<BudgetHistoryDetailDto> searchHistoryList(Integer year, Pageable pageable) {

        return repository.search(year, pageable);
    }

    @Override
    public BudgetHistoryDetailDto getHistory(Integer id) {
        return repository.findDtoById(id)
                .orElseThrow(NotFoundBudgetHistoryException::new);
    }

    @Override
    public List<Integer> getAllYearOfHistory() {
        return repository.findAllYear();
    }
}
