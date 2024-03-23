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
import com.inhabas.api.domain.board.exception.OnlyWriterModifiableException;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.exception.InProcessModifiableException;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;

@Service
@RequiredArgsConstructor
public class BudgetApplicationServiceImpl implements BudgetApplicationService {

  private final BudgetApplicationRepository budgetApplicationRepository;
  private final MemberRepository memberRepository;
  private final MenuRepository menuRepository;
  private final BoardFileRepository boardFileRepository;
  private static final Integer BUDGET_APPLICATION_MENU_ID = 14;

  @Transactional(readOnly = true)
  @Override
  public List<BudgetApplicationDto> getApplications(RequestStatus status) {
    return budgetApplicationRepository.search(status);
  }

  @Transactional(readOnly = true)
  @Override
  public BudgetApplicationDetailDto getApplicationDetails(Long applicationId) {

    BudgetSupportApplication application =
        budgetApplicationRepository.findById(applicationId).orElseThrow(NotFoundException::new);

    ClassifiedFiles classifiedFiles = ClassifyFiles.classifyFiles(application.getFiles());

    return BudgetApplicationDetailDto.builder()
        .id(application.getId())
        .dateUsed(application.getDateUsed())
        .dateCreated(application.getDateCreated())
        .dateUpdated(application.getDateUpdated())
        .title(application.getTitle())
        .details(application.getDetails())
        .outcome(application.getOutcome())
        .account(application.getAccount())
        .applicant(application.getApplicant())
        .memberInCharge(application.getMemberInCharge())
        .status(application.getStatus())
        .rejectReason(application.getRejectReason())
        .receipts(classifiedFiles.getImages())
        .build();
  }

  @Transactional
  @Override
  public Long registerApplication(BudgetApplicationRegisterForm form, Long memberId) {
    Member applicant =
        memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu =
        menuRepository.findById(BUDGET_APPLICATION_MENU_ID).orElseThrow(NotFoundException::new);
    BudgetSupportApplication application =
        form.toEntity(menu, applicant).writtenBy(applicant, BudgetSupportApplication.class);

    List<String> fileIdList = form.getFiles();
    List<BoardFile> boardFileList =
        boardFileRepository.getAllByIdInAndUploader(fileIdList, applicant);
    application.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(application);
    }
    application.updateFiles(boardFileList);
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

    if (!application.isWrittenBy(applicant)) {
      throw new OnlyWriterModifiableException();
    }

    application.modify(
        form.getTitle(),
        form.getDateUsed(),
        form.getDetails(),
        form.getOutcome(),
        form.getAccount());

    List<String> fileIdList = form.getFiles();
    List<BoardFile> boardFileList =
        boardFileRepository.getAllByIdInAndUploader(fileIdList, applicant);
    application.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(application);
    }
  }

  @Transactional
  @Override
  public void deleteApplication(Long applicationId, Long memberId) {
    BudgetSupportApplication application =
        budgetApplicationRepository.findById(applicationId).orElseThrow(NotFoundException::new);

    if (!application.isPending()) {
      throw new InProcessModifiableException();
    }

    budgetApplicationRepository.deleteById(applicationId);
  }
}
