package com.inhabas.api.domain.budget.converter;

import static com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus.*;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

import org.junit.jupiter.api.BeforeEach;

@DefaultDataJpaTest
public class StatusAttributeConverterTest {

  @Autowired private BudgetApplicationRepository repository;

  @Autowired private EntityManager em;

  private Member member;

  @BeforeEach
  public void setUp() {
    member = MemberTest.basicMember1();
    em.persist(member);
  }

  //    @DisplayName("Waiting 상태가 db에 1로 저장된다.")
  //    @Test
  //    public void convertWaitingStatusTo1Test() {
  //        //given
  //        BudgetSupportApplication application =
  //                new BudgetSupportApplication("스터디 지원금 신청",
  //                        LocalDateTime.of(2020, 1, 1, 1, 1, 1),
  //                        "머신러닝 논문 스터디 온라인 강의비 청구", 10000, "국민 123 홍길동", member.getId());
  //        repository.save(application);
  //
  //        Query query = em.createNativeQuery("select * from budget_support_application where
  // status = :status", BudgetSupportApplication.class);
  //        query.setParameter("status", 1);
  //        BudgetSupportApplication result = (BudgetSupportApplication) query.getSingleResult();
  //
  //
  //        ApplicationStatus status = (ApplicationStatus) ReflectionTestUtils.getField(result,
  // "status");
  //        assertThat(status).isEqualTo(ApplicationStatus.WAITING);
  //    }

  //    @DisplayName("APPROVED 상태가 db에 3로 저장된다.")
  //    @Test
  //    public void convertApprovedStatusTo2Test() {
  //        //given
  //        BudgetSupportApplication application =
  //                new BudgetSupportApplication("스터디 지원금 신청",
  //                        LocalDateTime.of(2020, 1, 1, 1, 1, 1),
  //                        "머신러닝 논문 스터디 온라인 강의비 청구", 10000, "국민 123 홍길동", member.getId());
  //        ReflectionTestUtils.setField(application, "status", APPROVED);
  //        repository.save(application);
  //
  //        Query query = em.createNativeQuery("select * from budget_support_application where
  // status = :status", BudgetSupportApplication.class);
  //        query.setParameter("status", 3);
  //        BudgetSupportApplication result = (BudgetSupportApplication) query.getSingleResult();
  //
  //
  //        ApplicationStatus status = (ApplicationStatus) ReflectionTestUtils.getField(result,
  // "status");
  //        assertThat(status).isEqualTo(ApplicationStatus.APPROVED);
  //    }

  //    @DisplayName("DENIED 상태가 db에 2로 저장된다.")
  //    @Test
  //    public void convertDeniedStatusTo3Test() {
  //        //given
  //        BudgetSupportApplication application =
  //                new BudgetSupportApplication("스터디 지원금 신청",
  //                        LocalDateTime.of(2020, 1, 1, 1, 1, 1),
  //                        "머신러닝 논문 스터디 온라인 강의비 청구", 10000, "국민 123 홍길동", member.getId());
  //        ReflectionTestUtils.setField(application, "status", DENIED);
  //        repository.save(application);
  //
  //        Query query = em.createNativeQuery("select * from budget_support_application where
  // status = :status", BudgetSupportApplication.class);
  //        query.setParameter("status", 2);
  //        BudgetSupportApplication result = (BudgetSupportApplication) query.getSingleResult();
  //
  //
  //        ApplicationStatus status = (ApplicationStatus) ReflectionTestUtils.getField(result,
  // "status");
  //        assertThat(status).isEqualTo(ApplicationStatus.DENIED);
  //    }

  //    @DisplayName("PROCESSED 상태가 db에 4로 저장된다.")
  //    @Test
  //    public void convertProcessedStatusTo3Test() {
  //        //given
  //        BudgetSupportApplication application =
  //                new BudgetSupportApplication("스터디 지원금 신청",
  //                        LocalDateTime.of(2020, 1, 1, 1, 1, 1),
  //                        "머신러닝 논문 스터디 온라인 강의비 청구", 10000, "국민 123 홍길동", member.getId());
  //        ReflectionTestUtils.setField(application, "status", PROCESSED);
  //        repository.save(application);
  //
  //        Query query = em.createNativeQuery("select * from budget_support_application where
  // status = :status", BudgetSupportApplication.class);
  //        query.setParameter("status", 4);
  //        BudgetSupportApplication result = (BudgetSupportApplication) query.getSingleResult();
  //
  //
  //        ApplicationStatus status = (ApplicationStatus) ReflectionTestUtils.getField(result,
  // "status");
  //        assertThat(status).isEqualTo(ApplicationStatus.PROCESSED);
  //    }
}
