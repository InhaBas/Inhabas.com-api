package com.inhabas.api.domain.budget.usecase;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.budget.repository.BudgetHistoryRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;

@Service
@RequiredArgsConstructor
public class BudgetApplicationProcessorImpl implements BudgetApplicationProcessor {

  private final BudgetApplicationRepository applicationRepository;
  private final BudgetHistoryRepository historyRepository;
  private final MemberRepository memberRepository;
  private final MenuRepository menuRepository;
  private static final Integer BUDGET_APPLICATION_MENU_ID = 14;

  @Transactional
  @Override
  public void process(
      Long applicationId, BudgetApplicationStatusChangeRequest request, Long memberId) {

    Member secretary =
        memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    BudgetSupportApplication application =
        applicationRepository.findById(applicationId).orElseThrow(NotFoundException::new);
    Menu menu =
        menuRepository.findById(BUDGET_APPLICATION_MENU_ID).orElseThrow(NotFoundException::new);

    switch (request.getStatus()) {
      case PENDING:
        application.pending();
        break;

      case APPROVED:
        application.approve(secretary);
        break;

      case REJECTED:
        application.reject(request.getRejectReason(), secretary);
        break;

      case COMPLETED:
        application.complete(secretary);

        // application to history
        transformToHistory(application, secretary, menu);
        break;
    }
  }

  private void transformToHistory(
      BudgetSupportApplication application, Member secretary, Menu menu) {
    BudgetHistory transformedHistory =
        historyRepository.save(application.makeHistory(secretary, menu));

    // application 에 있던 file 들을 history 에 복사
    List<BoardFile> files = application.getFiles();
    List<BoardFile> copiedFiles = new ArrayList<>();
    for (BoardFile file : files) {
      BoardFile copiedFile = file.copyFileWithNewId();
      copiedFiles.add(copiedFile);
      copiedFile.toBoard(transformedHistory);
    }

    transformedHistory.updateFiles(copiedFiles);
  }
}
