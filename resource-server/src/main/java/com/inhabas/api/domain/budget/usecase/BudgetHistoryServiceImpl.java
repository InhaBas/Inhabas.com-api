package com.inhabas.api.domain.budget.usecase;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.exception.OnlyWriterUpdateException;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryListResponse;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetHistoryServiceImpl implements BudgetHistoryService {

  private final BudgetHistoryRepository budgetHistoryRepository;
  private final MemberRepository memberRepository;
  @Override
  @Transactional
  public Long createNewHistory(BudgetHistoryCreateForm form, Long secretaryId) {
    Member secretary = memberRepository.findById(secretaryId)
        .orElseThrow(MemberNotFoundException::new);
    Member memberReceived = memberRepository.findById(form.getMemberIdReceived())
        .orElseThrow(MemberNotFoundException::new);
    BudgetHistory newHistory = form.toEntity(secretary, memberReceived);

    return budgetHistoryRepository.save(newHistory).getId();
  }

  @Override
  @Transactional
  public void modifyHistory(Long historyId, BudgetHistoryCreateForm form, Long secretaryId) {
    Member secretary = memberRepository.findById(secretaryId)
        .orElseThrow(MemberNotFoundException::new);
    Member memberReceived = memberRepository.findById(form.getMemberIdReceived())
        .orElseThrow(MemberNotFoundException::new);

    BudgetHistory budgetHistory =
        budgetHistoryRepository.findById(historyId).orElseThrow(NotFoundException::new);

    budgetHistory.modify(
        secretary,
        form.getIncome(),
        form.getOutcome(),
        form.getDateUsed(),
        form.getTitle(),
        form.getDetails(),
        memberReceived);

    budgetHistoryRepository.save(budgetHistory);
  }

  @Override
  @Transactional
  public void deleteHistory(Long historyId, Long secretaryId) {
    Member secretary = memberRepository.findById(secretaryId)
        .orElseThrow(MemberNotFoundException::new);
    BudgetHistory budgetHistory =
        budgetHistoryRepository.findById(historyId).orElseThrow(NotFoundException::new);

    if (budgetHistory.cannotModifiableBy(secretary)) {
      throw new OnlyWriterUpdateException();
    }

    budgetHistoryRepository.deleteById(historyId);
  }

  @Override
  public BudgetHistoryListResponse searchHistoryList(Integer year, Pageable pageable) {

    Page<BudgetHistoryDetailDto> listPage = budgetHistoryRepository.search(year, pageable);
    Integer balance = budgetHistoryRepository.getBalance();
    return new BudgetHistoryListResponse(listPage, balance);
  }

  @Override
  public BudgetHistoryDetailDto getHistory(Long id) {
    return budgetHistoryRepository.findDtoById(id).orElseThrow(NotFoundException::new);
  }

  @Override
  public List<Integer> getAllYearOfHistory() {
    return budgetHistoryRepository.findAllYear();
  }
}
