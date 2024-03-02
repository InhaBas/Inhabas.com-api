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
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.exception.S3UploadFailedException;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.global.util.FileUtil;

@Service
@RequiredArgsConstructor
public class BudgetApplicationServiceImpl implements BudgetApplicationService {

  private final BudgetApplicationRepository budgetApplicationRepository;
  private final MemberRepository memberRepository;
  private final MenuRepository menuRepository;
  private final S3Service s3Service;
  private static final Integer BUDGET_APPLICATION_MENU_ID = 14;
  private static final String DIR_NAME = "budget/";

  @Transactional(readOnly = true)
  @Override
  public List<BudgetApplicationDto> getApplications(RequestStatus status) {
    return budgetApplicationRepository.search(status);
  }

  @Transactional(readOnly = true)
  @Override
  public BudgetApplicationDetailDto getApplicationDetails(Long applicationId) {

    return budgetApplicationRepository
        .findDtoById(applicationId)
        .orElseThrow(NotFoundException::new);
  }

  @Transactional
  @Override
  public Long registerApplication(
      BudgetApplicationRegisterForm form, List<MultipartFile> files, Long memberId) {
    Member applicant =
        memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu =
        menuRepository.findById(BUDGET_APPLICATION_MENU_ID).orElseThrow(NotFoundException::new);
    BudgetSupportApplication application =
        form.toEntity(menu, applicant).writtenBy(applicant, BudgetSupportApplication.class);

    return updateBudgetFiles(files, application);
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
        form.getAccount(),
        applicant);

    budgetApplicationRepository.save(application);
  }

  @Transactional
  @Override
  public void deleteApplication(Long applicationId, Long memberId) {
    budgetApplicationRepository.deleteById(applicationId);
  }

  private Long updateBudgetFiles(List<MultipartFile> files, BudgetSupportApplication application) {
    List<BoardFile> updateReceipts = new ArrayList<>();
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
                      return BoardFile.builder()
                          .name(file.getOriginalFilename())
                          .url(url)
                          .board(application)
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

    application.updateFiles(updateReceipts);
    return budgetApplicationRepository.save(application).getId();
  }
}
