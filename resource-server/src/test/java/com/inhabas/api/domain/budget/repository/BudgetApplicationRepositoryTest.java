package com.inhabas.api.domain.budget.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuExampleTest;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest;
import com.inhabas.testAnnotation.DefaultDataJpaTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class BudgetApplicationRepositoryTest {

  @Autowired private BudgetApplicationRepository budgetApplicationRepository;
  @Autowired private TestEntityManager em;
  private Member member;
  private Menu menu;
  private MenuGroup menuGroup;
  private static final String APPLICATION_TITLE = "title";
  private static final String APPLICATION_DETAILS = "details";
  private static final String ACCOUNT_NUMBER = "123-123-123";
  private static final Integer APPLICATION_OUTCOME = 10000;
  private static final RequestStatus INITIAL_REQUEST_STATUS = RequestStatus.PENDING;

  @BeforeEach
  public void setUp() {
    member = em.persist(MemberTest.basicMember1());
    menuGroup = em.persist(MenuGroupExampleTest.getBudgetMenuGroup());
    menu = em.persist(MenuExampleTest.getBudgetHistoryMenu(menuGroup));
  }

  @DisplayName("처리 완료된 것을 제외한 모든 예산지원신청서를 검색한다.")
  @Test
  public void searchAllTest() {
    // given
    List<BudgetSupportApplication> applicationList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      BudgetSupportApplication application =
          createSupportApplication(member).writtenBy(member, BudgetSupportApplication.class);
      applicationList.add(application);
    }
    budgetApplicationRepository.saveAll(applicationList);

    // when
    List<BudgetApplicationDto> dtoList = budgetApplicationRepository.search(null);

    // then
    assertThat(dtoList).hasSize(5);
  }

  @DisplayName("특정 상태의 예산지원신청서만 검색한다.")
  @Test
  public void searchSpecificApplicationsTest() {
    // given
    List<BudgetSupportApplication> applicationList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      BudgetSupportApplication application =
          createSupportApplication(member).writtenBy(member, BudgetSupportApplication.class);
      applicationList.add(application);
    }
    budgetApplicationRepository.saveAll(applicationList);

    // when
    List<BudgetApplicationDto> dtoList = budgetApplicationRepository.search(INITIAL_REQUEST_STATUS);

    // then
    assertThat(dtoList).hasSize(5);
  }

  @DisplayName("예산지원신청서는 작성 시점 기준 내림차순으로 정렬된다.")
  @Test
  public void searchOrderedByDateCreatedTest() {
    // given : 나중에 작성한 신청서일수록 사용일은 더 과거가 되도록 저장
    List<Long> savedIds = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      BudgetSupportApplication application =
          new BudgetSupportApplication(
                  menu,
                  APPLICATION_TITLE,
                  APPLICATION_DETAILS,
                  LocalDateTime.now().minusDays(i + 1),
                  ACCOUNT_NUMBER,
                  APPLICATION_OUTCOME,
                  member,
                  INITIAL_REQUEST_STATUS)
              .writtenBy(member, BudgetSupportApplication.class);
      savedIds.add(budgetApplicationRepository.save(application).getId());
    }

    // when
    List<BudgetApplicationDto> dtoList = budgetApplicationRepository.search(null);

    // then : 사용일 순서와 무관하게 최근에 작성한 신청서가 먼저 조회된다.
    Collections.reverse(savedIds);
    assertThat(dtoList).extracting(BudgetApplicationDto::getId).containsExactlyElementsOf(savedIds);
  }

  private Object getFieldValueByReflection(Object target, String fieldName) {
    return ReflectionTestUtils.getField(target, fieldName);
  }

  private BudgetSupportApplication createSupportApplication(Member applicant) {
    return new BudgetSupportApplication(
        menu,
        APPLICATION_TITLE,
        APPLICATION_DETAILS,
        LocalDateTime.now().minusDays(1),
        ACCOUNT_NUMBER,
        APPLICATION_OUTCOME,
        applicant,
        INITIAL_REQUEST_STATUS);
  }
}
