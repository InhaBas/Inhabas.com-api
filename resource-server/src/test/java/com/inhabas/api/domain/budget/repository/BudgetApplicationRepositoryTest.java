package com.inhabas.api.domain.budget.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import com.inhabas.api.domain.member.domain.MemberTest;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

@DefaultDataJpaTest
public class BudgetApplicationRepositoryTest {

    @Autowired
    private BudgetApplicationRepository repository;

    @Autowired
    private TestEntityManager em;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = em.persist(MemberTest.MEMBER1());
    }

    @Test
    public void findDtoByIdTest() {
        //given
        BudgetSupportApplication application =
                new BudgetSupportApplication("스터디 지원금 신청",
                        LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                        "머신러닝 논문 스터디 온라인 강의비 청구", 10000, "국민 123 홍길동", member.getId());
        repository.save(application);
        Integer id = (Integer) ReflectionTestUtils.getField(application, "id");

        //when
        BudgetApplicationDetailDto detailDto = repository.findDtoById(id)
                .orElseThrow();

        //then
        assertThat(getField(detailDto, "id")).isEqualTo(id);
        assertThat(getField(detailDto, "outcome")).isEqualTo(10000);
        assertThat(getField(detailDto, "applicationWriterName")).isEqualTo("유동현");
        assertThat(getField(detailDto, "memberIdInCharge")).isNull();
    }

    @DisplayName("처리 완료된 것을 제외한 모든 예산지원신청서를 검색한다.")
    @Test
    public void searchAllTest() {
        //given
        List<BudgetSupportApplication> applicationList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BudgetSupportApplication application =
                    new BudgetSupportApplication("스터디 지원금 신청",
                            LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                            "머신러닝 논문 스터디 온라인 강의비 청구", 10000, "국민 123 홍길동", member.getId());
            ReflectionTestUtils.setField(application, "status", ApplicationStatus.values()[i % 4]);
            applicationList.add(application);
        }
        repository.saveAll(applicationList);

        //when
        Page<BudgetApplicationListDto> page =
                repository.search(null, PageRequest.of(1, 10, Sort.Direction.DESC, "dateUsed"));

        //then
        assertThat(page.getTotalElements()).isEqualTo(15);
        assertThat(page.getNumberOfElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    private Object getField(Object target, String fieldName) {
        return ReflectionTestUtils.getField(target, fieldName);
    }

    @DisplayName("특정 상태의 예산지원신청서만 검색한다.")
    @Test
    public void searchSpecificApplicationsTest() {
        //given
        List<BudgetSupportApplication> applicationList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BudgetSupportApplication application =
                    new BudgetSupportApplication("스터디 지원금 신청",
                            LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                            "머신러닝 논문 스터디 온라인 강의비 청구", 10000, "국민 123 홍길동", member.getId());
            ReflectionTestUtils.setField(application, "status", ApplicationStatus.values()[i % 4]);
            applicationList.add(application);
        }
        repository.saveAll(applicationList);

        //when
        Page<BudgetApplicationListDto> page =
                repository.search(ApplicationStatus.DENIED, PageRequest.of(0, 15, Sort.Direction.DESC, "dateUsed"));

        //then
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumberOfElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getContent())
                .extracting("status")
                .containsOnly(ApplicationStatus.DENIED);
    }
}
