package com.inhabas.api.domain.budget.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.MenuExampleTest;
import com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class BudgetHistoryTest {

  @Test
  void createBudgetHistory_Success() {
    // Given
    LocalDateTime dateUsed = LocalDateTime.now();
    Member memberInCharge = MemberTest.chiefMember();
    ReflectionTestUtils.setField(memberInCharge, "id", 1L);
    Member memberReceived = MemberTest.basicMember1();
    ReflectionTestUtils.setField(memberReceived, "id", 2L);

    // When
    BudgetHistory budgetHistory = new BudgetHistory(
        "Title",
        MenuExampleTest.getBudgetHistoryMenu(MenuGroupExampleTest.getBudgetMenuGroup()),
        "Details",
        dateUsed,
        memberInCharge,
        "Account info",
        1000,
        500,
        memberReceived);

    // Then
    assertThat(budgetHistory.getDetails()).isEqualTo("Details");
    assertThat(budgetHistory.getIncome()).isEqualTo(1000);
    assertThat(budgetHistory.getOutcome()).isEqualTo(500);
    assertThat(budgetHistory.getAccount()).isEqualTo("Account info");
  }

  @Test
  void modifyBudgetHistory_Success() {
    // Given
    Member memberInCharge = MemberTest.chiefMember();
    ReflectionTestUtils.setField(memberInCharge, "id", 1L);
    Member newMemberReceived = MemberTest.basicMember1();
    ReflectionTestUtils.setField(newMemberReceived, "id", 2L);
    BudgetHistory budgetHistory = setupBudgetHistory(memberInCharge);
    ReflectionTestUtils.setField(budgetHistory, "id", 1L);
    LocalDateTime newDateUsed = LocalDateTime.now();
    String newTitle = "New Title";
    String newDetails = "Updated Details";
    Integer newIncome = 2000;
    Integer newOutcome = 1000;

    // when
    budgetHistory.modify(
        memberInCharge,
        newIncome,
        newOutcome,
        newDateUsed,
        newTitle,
        newDetails,
        newMemberReceived
    );

    // then
    assertThat(budgetHistory.getDetails()).isEqualTo(newDetails);
    assertThat(budgetHistory.getIncome()).isEqualTo(newIncome);
    assertThat(budgetHistory.getOutcome()).isEqualTo(newOutcome);
    assertThat(budgetHistory.getTitle()).isEqualTo(newTitle);
    assertThat(budgetHistory.getMemberReceived()).isEqualTo(newMemberReceived);
  }

  private BudgetHistory setupBudgetHistory(Member memberInCharge) {
    return BudgetHistory.builder()
        .title("Title")
        .menu(MenuExampleTest.getBudgetHistoryMenu(MenuGroupExampleTest.getBudgetMenuGroup()))
        .details("Details")
        .dateUsed(LocalDateTime.now())
        .memberInCharge(memberInCharge)
        .account("Account info")
        .income(1000)
        .outcome(500)
        .memberReceived(memberInCharge)
        .build();
  }

}