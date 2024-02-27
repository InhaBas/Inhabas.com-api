package com.inhabas.api.domain.budget.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.exception.OnlyWriterUpdateException;
import com.inhabas.api.domain.board.exception.S3UploadFailedException;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDto;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import com.inhabas.api.domain.file.domain.BudgetFile;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;
import com.inhabas.api.global.util.FileUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BudgetHistoryServiceImpl implements BudgetHistoryService {

  private final BudgetHistoryRepository budgetHistoryRepository;
  private final MemberRepository memberRepository;
  private final S3Service s3Service;
  private static final String DIR_NAME = "budget/";

  @Override
  @Transactional
  public Long createHistory(
      BudgetHistoryCreateForm form, List<MultipartFile> files, Long secretaryId) {

    Member secretary =
        memberRepository.findById(secretaryId).orElseThrow(MemberNotFoundException::new);
    Member memberReceived =
        memberRepository
            .findByStudentId_IdAndName_Value(
                form.getMemberStudentIdReceived(), form.getMemberNameReceived())
            .orElseThrow(MemberNotFoundException::new);
    BudgetHistory newHistory = form.toEntity(secretary, memberReceived);

    return updateBudgetFiles(files, newHistory);
  }

  @Override
  @Transactional
  public void modifyHistory(
      Long historyId, BudgetHistoryCreateForm form, List<MultipartFile> files, Long secretaryId) {
    Member secretary =
        memberRepository.findById(secretaryId).orElseThrow(MemberNotFoundException::new);
    Member memberReceived =
        memberRepository
            .findByStudentId_IdAndName_Value(
                form.getMemberStudentIdReceived(), form.getMemberNameReceived())
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

    updateBudgetFiles(files, budgetHistory);
  }

  @Override
  @Transactional
  public void deleteHistory(Long historyId, Long secretaryId) {
    Member secretary =
        memberRepository.findById(secretaryId).orElseThrow(MemberNotFoundException::new);
    BudgetHistory budgetHistory =
        budgetHistoryRepository.findById(historyId).orElseThrow(NotFoundException::new);

    if (budgetHistory.cannotModifiableBy(secretary)) {
      throw new OnlyWriterUpdateException();
    }

    budgetHistoryRepository.deleteById(historyId);
  }

  @Override
  public List<BudgetHistoryDto> searchHistoryList(Integer year) {

    List<BudgetHistoryDto> dtoList = budgetHistoryRepository.search(year);
    return dtoList;
  }

  @Override
  public BudgetHistoryDetailDto getHistory(Long id) {
    BudgetHistory history =
        budgetHistoryRepository.findById(id).orElseThrow(NotFoundException::new);

    ClassifiedFiles classifiedFiles = ClassifyFiles.classifyFiles(history.getReceipts());

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
  public List<Integer> getAllYearOfHistory() {
    return budgetHistoryRepository.findAllYear();
  }

  @Override
  public Integer getBalance() {
    return budgetHistoryRepository.getBalance();
  }

  private Long updateBudgetFiles(List<MultipartFile> files, BudgetHistory budgetHistory) {
    List<BudgetFile> updateReceipts = new ArrayList<>();
    List<String> urlListForDelete = new ArrayList<>();

    if (files != null) {
      try {
        updateReceipts =
            files.stream()
                .map(
                    file -> {
                      String path = FileUtil.generateFileName(file, DIR_NAME);
                      String url = s3Service.uploadS3Image(file, path);
                      urlListForDelete.add(url);
                      return BudgetFile.builder()
                          .name(file.getOriginalFilename())
                          .url(url)
                          .board(budgetHistory)
                          .build();
                    })
                .collect(Collectors.toList());

      } catch (RuntimeException e) {
        for (String url : urlListForDelete) {
          s3Service.deleteS3File(url);
        }
        throw new S3UploadFailedException();
      }
    }

    budgetHistory.updateReceipts(updateReceipts);
    return budgetHistoryRepository.save(budgetHistory).getId();
  }
}
