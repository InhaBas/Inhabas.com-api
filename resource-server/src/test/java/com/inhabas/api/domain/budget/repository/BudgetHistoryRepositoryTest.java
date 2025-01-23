package com.inhabas.api.domain.budget.repository;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static com.inhabas.api.domain.member.domain.entity.MemberTest.secretaryMember;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDto;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuExampleTest;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class BudgetHistoryRepositoryTest {

  @Autowired private TestEntityManager em;
  @Autowired private BudgetHistoryRepository budgetHistoryRepository;

  private Member memberInCharge;
  private Member memberReceived;
  private Menu menu;
  private MenuGroup menuGroup;
  private static final String HISTORY_TITLE = "title";
  private static final String HISTORY_DETAILS = "details";
  private static final Integer HISTORY_OUTCOME = 10000;
  private static final String ACCOUNT_NUMBER = "123-123-123";
  private static final LocalDateTime HISTORY_DATE_USED = LocalDateTime.now().minusDays(10L);

  @BeforeEach
  public void setUp() {
    memberInCharge = em.persist(secretaryMember());
    memberReceived = em.persist(basicMember1());
    menuGroup = em.persist(MenuGroupExampleTest.getBudgetMenuGroup());
    menu = em.persist(MenuExampleTest.getBudgetHistoryMenu(menuGroup));
  }

  @DisplayName("예산 내역을 하나 조회한다.")
  @Test
  public void fetchOneBudgetHistoryTest() {
    // given
    BudgetHistory history =
        budgetHistoryRepository.save(
            BudgetHistory.builder()
                .title(HISTORY_TITLE)
                .menu(menu)
                .details(HISTORY_DETAILS)
                .dateUsed(HISTORY_DATE_USED)
                .account(ACCOUNT_NUMBER)
                .income(0)
                .outcome(HISTORY_OUTCOME)
                .memberInCharge(memberInCharge)
                .memberReceived(memberReceived)
                .build()
                .writtenBy(memberInCharge, BudgetHistory.class));
    Long id = (Long) ReflectionTestUtils.getField(history, "id");

    // when
    BudgetHistory budgetHistory = budgetHistoryRepository.findById(id).orElseThrow();

    // then
    assertThat(getField(budgetHistory, "dateCreated")).isNotNull();
    assertThat(budgetHistory.getTitle()).isEqualTo(HISTORY_TITLE);
    assertThat(budgetHistory.getOutcome()).isEqualTo(HISTORY_OUTCOME);
    assertThat(getField(budgetHistory, "memberInCharge")).isEqualTo(memberInCharge);
    assertThat(getField(budgetHistory, "memberReceived")).isEqualTo(memberReceived);
  }

  @DisplayName("모든 년도 예산 내역 조회")
  @Test
  public void fetchBudgetHistoryPageTest() {
    // given
    List<BudgetHistory> histories = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      histories.add(
          BudgetHistory.builder()
              .title(HISTORY_TITLE + i)
              .menu(menu)
              .details(HISTORY_DETAILS)
              .dateUsed(HISTORY_DATE_USED.plusDays(i))
              .account(ACCOUNT_NUMBER)
              .income(0)
              .outcome(HISTORY_OUTCOME)
              .memberInCharge(memberInCharge)
              .memberReceived(memberReceived)
              .build()
              .writtenBy(memberInCharge, BudgetHistory.class));
    }
    budgetHistoryRepository.saveAll(histories);

    // when
    List<BudgetHistoryDto> dtoList = budgetHistoryRepository.search(null);

    // then
    assertThat(dtoList).hasSize(5);
    assertThat(dtoList.get(0).getTitle()).isEqualTo(HISTORY_TITLE + 4);
    assertThat(dtoList.get(1).getTitle()).isEqualTo(HISTORY_TITLE + 3);
    assertThat(dtoList.get(2).getTitle()).isEqualTo(HISTORY_TITLE + 2);
    assertThat(dtoList.get(3).getTitle()).isEqualTo(HISTORY_TITLE + 1);
    assertThat(dtoList.get(4).getTitle()).isEqualTo(HISTORY_TITLE + 0);
  }

  @DisplayName("해당 년도 예산 내역 조회")
  @Test
  public void searchBudgetHistoryByYearTest() {
    // given
    List<BudgetHistory> histories = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      histories.add(
          BudgetHistory.builder()
              .title(HISTORY_TITLE)
              .menu(menu)
              .details(HISTORY_DETAILS)
              .dateUsed(HISTORY_DATE_USED)
              .account(ACCOUNT_NUMBER)
              .income(0)
              .outcome(HISTORY_OUTCOME)
              .memberInCharge(memberInCharge)
              .memberReceived(memberReceived)
              .build()
              .writtenBy(memberInCharge, BudgetHistory.class));
    }
    budgetHistoryRepository.saveAll(histories);

    // when
    List<BudgetHistoryDto> dtoList = budgetHistoryRepository.search(HISTORY_DATE_USED.getYear());

    // then
    assertThat(dtoList).hasSize(5);
  }

  @DisplayName("회계내역이 있는 년도 조회")
  @Test
  public void getAllYearOfHistory() {
    // given
    List<BudgetHistory> histories = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      histories.add(
          BudgetHistory.builder()
              .title(HISTORY_TITLE)
              .menu(menu)
              .details(HISTORY_DETAILS)
              .dateUsed(HISTORY_DATE_USED.minusYears(i))
              .account(ACCOUNT_NUMBER)
              .income(0)
              .outcome(HISTORY_OUTCOME)
              .memberInCharge(memberInCharge)
              .memberReceived(memberReceived)
              .build()
              .writtenBy(memberInCharge, BudgetHistory.class));
    }
    budgetHistoryRepository.saveAll(histories);

    // when
    List<Integer> allYear = budgetHistoryRepository.findAllYear();

    // then
    assertThat(allYear).containsExactly(2025, 2024, 2023, 2022, 2021, 2020);
    assertThat(allYear).hasSize(6);
  }

  @DisplayName("잔액 조회")
  @Test
  public void getBalanceTest() {
    // given
    budgetHistoryRepository.save(
        BudgetHistory.builder()
            .title(HISTORY_TITLE)
            .menu(menu)
            .details(HISTORY_DETAILS)
            .dateUsed(HISTORY_DATE_USED)
            .account(ACCOUNT_NUMBER)
            .income(HISTORY_OUTCOME)
            .outcome(0)
            .memberInCharge(memberInCharge)
            .memberReceived(memberInCharge)
            .build()
            .writtenBy(memberInCharge, BudgetHistory.class));
    budgetHistoryRepository.save(
        BudgetHistory.builder()
            .title(HISTORY_TITLE)
            .menu(menu)
            .details(HISTORY_DETAILS)
            .dateUsed(HISTORY_DATE_USED)
            .account(ACCOUNT_NUMBER)
            .income(0)
            .outcome(HISTORY_OUTCOME)
            .memberInCharge(memberInCharge)
            .memberReceived(memberReceived)
            .build()
            .writtenBy(memberInCharge, BudgetHistory.class));

    // when
    Integer balance = budgetHistoryRepository.getBalance();

    // then
    assertThat(balance).isEqualTo(0);
  }

  private Object getField(Object target, String field) {
    return ReflectionTestUtils.getField(target, field);
  }
}
