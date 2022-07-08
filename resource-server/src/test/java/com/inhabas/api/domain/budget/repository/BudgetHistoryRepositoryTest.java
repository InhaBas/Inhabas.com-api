package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.inhabas.api.domain.member.domain.MemberTest.MEMBER1;
import static com.inhabas.api.domain.member.domain.MemberTest.MEMBER2;
import static org.assertj.core.api.Assertions.assertThat;

@DefaultDataJpaTest
public class BudgetHistoryRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private BudgetHistoryRepository budgetHistoryRepository;

    private MemberId received, inCharge;

    @BeforeEach
    public void setUp() {
        Member member1 = MEMBER1();
        Member member2 = MEMBER2();
        em.persist(member1);
        em.persist(member2);
        received = member1.getId();
        inCharge = member2.getId();
    }

    @DisplayName("예산 내역을 하나 조회한다.")
    @Test
    public void fetchOneBudgetHistoryTest() {
        //given
        BudgetHistory history = budgetHistoryRepository.save(
                BudgetHistory.builder()
                        .title("간식비")
                        .details("과자")
                        .income(0)
                        .outcome(500000)
                        .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                        .personReceived(received)
                        .personInCharge(inCharge)
                        .build());
        Integer id = (Integer) ReflectionTestUtils.getField(history, "id");

        //when
        BudgetHistoryDetailDto budgetHistoryDetailDto = budgetHistoryRepository.findDtoById(id)
                .orElseThrow();

        //then
        assertThat(budgetHistoryDetailDto.getDateCreated()).isNotNull();
        assertThat(budgetHistoryDetailDto.getTitle()).isEqualTo("간식비");
        assertThat(budgetHistoryDetailDto.getOutcome()).isEqualTo(500000);
        assertThat(budgetHistoryDetailDto.getReceivedMemberName()).isEqualTo("유동현");
        assertThat(budgetHistoryDetailDto.getMemberNameInCharge()).isEqualTo("김민겸");
    }

    @DisplayName("예산 내역 페이지 객체를 가져온다.")
    @Test
    public void fetchBudgetHistoryPageTest() {

        //given
        List<BudgetHistory> histories = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            histories.add(
                    BudgetHistory.builder()
                            .title("간식비" + i)
                            .details("과자" + i)
                            .income(0)
                            .outcome(500000)
                            .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                            .personReceived(received)
                            .personInCharge(inCharge)
                            .build());
        }
        budgetHistoryRepository.saveAll(histories);

        //when
        Page<BudgetHistoryDetailDto> page = budgetHistoryRepository.findAllByPageable(
                PageRequest.of(1, 20, Direction.DESC, "dateUsed"));


        //then
        assertThat(page.getTotalElements()).isEqualTo(30);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0)).extracting("title").isEqualTo("간식비21");
    }
}
