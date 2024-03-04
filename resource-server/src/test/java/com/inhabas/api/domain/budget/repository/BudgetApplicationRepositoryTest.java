package com.inhabas.api.domain.budget.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuExampleTest;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

@DefaultDataJpaTest
public class BudgetApplicationRepositoryTest {

  @Autowired
  private BudgetApplicationRepository budgetApplicationRepository;
  @Autowired
  private TestEntityManager em;
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

  @DisplayName("id가 일치하는 history 상세 정보 dto 조회")
  @Test
  public void findDtoByIdTest() {

    //given
    BudgetSupportApplication application = createSupportApplication(member).writtenBy(member,
        BudgetSupportApplication.class);
    budgetApplicationRepository.save(application);
    Long id = (Long) ReflectionTestUtils.getField(application, "id");

    //when
    BudgetApplicationDetailDto detailDto = budgetApplicationRepository.findDtoById(id)
        .orElseThrow();

    //then
    assertThat(getFieldValueByReflection(detailDto, "id")).isEqualTo(id);
    assertThat(getFieldValueByReflection(detailDto, "title")).isEqualTo(APPLICATION_TITLE);
    assertThat(getFieldValueByReflection(detailDto, "outcome")).isEqualTo(APPLICATION_OUTCOME);
    assertThat(getFieldValueByReflection(detailDto, "memberIdInCharge")).isNull();
  }

  @DisplayName("처리 완료된 것을 제외한 모든 예산지원신청서를 검색한다.")
  @Test
  public void searchAllTest() {
    //given
    List<BudgetSupportApplication> applicationList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      BudgetSupportApplication application = createSupportApplication(member).writtenBy(member,
          BudgetSupportApplication.class);
      applicationList.add(application);
    }
    budgetApplicationRepository.saveAll(applicationList);

    //when
    List<BudgetApplicationDto> dtoList =
        budgetApplicationRepository.search(null);

    //then
    assertThat(dtoList).hasSize(5);
  }

  @DisplayName("특정 상태의 예산지원신청서만 검색한다.")
  @Test
  public void searchSpecificApplicationsTest() {
    //given
    List<BudgetSupportApplication> applicationList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      BudgetSupportApplication application = createSupportApplication(member).writtenBy(member,
          BudgetSupportApplication.class);
      applicationList.add(application);
    }
    budgetApplicationRepository.saveAll(applicationList);

    //when
    List<BudgetApplicationDto> dtoList =
        budgetApplicationRepository.search(INITIAL_REQUEST_STATUS);

    //then
    assertThat(dtoList).hasSize(5);
  }

  private Object getFieldValueByReflection(Object target, String fieldName) {
    return ReflectionTestUtils.getField(target, fieldName);
  }

  private BudgetSupportApplication createSupportApplication(Member applicant) {
    return new BudgetSupportApplication(menu, APPLICATION_TITLE, APPLICATION_DETAILS,
        LocalDateTime.now().minusDays(1),
        ACCOUNT_NUMBER, APPLICATION_OUTCOME, applicant, INITIAL_REQUEST_STATUS);
  }
}
