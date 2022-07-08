package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BudgetHistoryService {

    void createNewHistory(BudgetHistoryCreateForm form, MemberId CFO);

    void modifyHistory(BudgetHistoryModifyForm historyId, MemberId CFO);

    void deleteHistory(Integer historyId, MemberId CFO);

    Page<BudgetHistoryDetailDto> searchHistoryList(Integer year, Pageable pageable);

    BudgetHistoryDetailDto getHistory(Integer id);
}
