package com.inhabas.api.domain.budget.usecase;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.exception.OnlyWriterUpdateException;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDto;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;

@Service
@RequiredArgsConstructor
public class BudgetHistoryServiceImpl implements BudgetHistoryService {

  private final BudgetHistoryRepository budgetHistoryRepository;
  private final MemberRepository memberRepository;
  private final MenuRepository menuRepository;
  private final BoardFileRepository boardFileRepository;
  private static final Integer BUDGET_HISTORY_MENU_ID = 15;

  @Override
  @Transactional
  public Long createHistory(BudgetHistoryCreateForm form, Long secretaryId) {
    Member secretary =
        memberRepository.findById(secretaryId).orElseThrow(MemberNotFoundException::new);
    Menu menu = menuRepository.findById(BUDGET_HISTORY_MENU_ID).orElseThrow(NotFoundException::new);
    Member memberReceived;
    if (form.isIncome()) {
      memberReceived = secretary;
    } else if (form.isOutcome()) {
      memberReceived =
          memberRepository
              .findByIdAndStudentId_IdAndName_Value(
                  form.getMemberIdReceived(),
                  form.getMemberStudentIdReceived(),
                  form.getMemberNameReceived())
              .orElseThrow(MemberNotFoundException::new);
    } else {
      throw new InvalidInputException();
    }

    BudgetHistory newHistory =
        form.toEntity(menu, secretary, memberReceived).writtenBy(secretary, BudgetHistory.class);

    List<String> fileIdList = form.getFiles();
    List<BoardFile> boardFileList =
        boardFileRepository.getAllByIdInAndUploader(fileIdList, secretary);
    newHistory.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(newHistory);
    }
    return budgetHistoryRepository.save(newHistory).getId();
  }

  @Override
  @Transactional
  public void modifyHistory(Long historyId, BudgetHistoryCreateForm form, Long secretaryId) {
    Member secretary =
        memberRepository.findById(secretaryId).orElseThrow(MemberNotFoundException::new);
    Member memberReceived;
    if (form.isIncome()) {
      memberReceived = secretary;
    } else if (form.isOutcome()) {
      memberReceived =
          memberRepository
              .findByIdAndStudentId_IdAndName_Value(
                  form.getMemberIdReceived(),
                  form.getMemberStudentIdReceived(),
                  form.getMemberNameReceived())
              .orElseThrow(MemberNotFoundException::new);
    } else {
      throw new InvalidInputException();
    }
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

    List<String> fileIdList = form.getFiles();
    List<BoardFile> boardFileList =
        boardFileRepository.getAllByIdInAndUploader(fileIdList, secretary);
    budgetHistory.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(budgetHistory);
    }
  }

  @Override
  @Transactional
  public void deleteHistory(Long historyId, Long secretaryId) {
    Member secretary =
        memberRepository.findById(secretaryId).orElseThrow(MemberNotFoundException::new);
    BudgetHistory budgetHistory =
        budgetHistoryRepository.findById(historyId).orElseThrow(NotFoundException::new);

    if (!budgetHistory.isWrittenBy(secretary)) {
      throw new OnlyWriterUpdateException();
    }

    budgetHistoryRepository.deleteById(historyId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BudgetHistoryDto> searchHistoryList(Integer year) {

    List<BudgetHistoryDto> dtoList = budgetHistoryRepository.search(year);
    return dtoList;
  }

  @Override
  @Transactional(readOnly = true)
  public BudgetHistoryDetailDto getHistory(Long id) {
    BudgetHistory history =
        budgetHistoryRepository.findById(id).orElseThrow(NotFoundException::new);

    ClassifiedFiles classifiedFiles = ClassifyFiles.classifyFiles(history.getFiles());

    return BudgetHistoryDetailDto.builder()
        .id(history.getId())
        .dateUsed(history.getDateUsed())
        .dateCreated(history.getDateCreated())
        .dateUpdated(history.getDateUpdated())
        .title(history.getTitle())
        .details(history.getDetails())
        .account(history.getAccount())
        .income(history.getIncome())
        .outcome(history.getOutcome())
        .memberStudentIdInCharge(history.getMemberInCharge().getStudentId())
        .memberNameInCharge(history.getMemberInCharge().getName())
        .memberStudentIdReceived(history.getMemberReceived().getStudentId())
        .memberNameReceived(history.getMemberReceived().getName())
        .receipts(classifiedFiles.getImages())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Integer> getAllYearOfHistory() {
    return budgetHistoryRepository.findAllYear();
  }

  @Override
  @Transactional(readOnly = true)
  public Integer getBalance() {
    return budgetHistoryRepository.getBalance();
  }
}
