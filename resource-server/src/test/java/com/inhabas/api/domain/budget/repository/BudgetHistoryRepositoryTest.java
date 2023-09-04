package com.inhabas.api.domain.budget.repository;

import static com.inhabas.api.domain.member.domain.MemberTest.basicMember1;
import static com.inhabas.api.domain.member.domain.MemberTest.basicMember2;
import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.util.ReflectionTestUtils;

@DefaultDataJpaTest
public class BudgetHistoryRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private BudgetHistoryRepository budgetHistoryRepository;

    private MemberId received, inCharge;

    @BeforeEach
    public void setUp() {
        Member member1 = basicMember1();
        Member member2 = basicMember2();
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
        assertThat(getField(budgetHistoryDetailDto, "dateCreated")).isNotNull();
        assertThat(getField(budgetHistoryDetailDto, "title")).isEqualTo("간식비");
        assertThat(getField(budgetHistoryDetailDto, "outcome")).isEqualTo(500000);
        assertThat(getField(budgetHistoryDetailDto, "receivedMemberName")).isEqualTo("유동현");
        assertThat(getField(budgetHistoryDetailDto, "memberNameInCharge")).isEqualTo("김민겸");
    }

    private Object getField(Object target, String field) {
        return ReflectionTestUtils.getField(target, field);
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
                            .dateUsed(LocalDateTime.of(2000, 1, i, 1, 1, 1))
                            .personReceived(received)
                            .personInCharge(inCharge)
                            .build());
        }
        budgetHistoryRepository.saveAll(histories);

        //when
        Page<BudgetHistoryDetailDto> page = budgetHistoryRepository.search(null,
                PageRequest.of(1, 20, Direction.DESC, "dateUsed"));

        //then
        assertThat(page.getTotalElements()).isEqualTo(30);
        assertThat(page.getNumberOfElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0)).extracting("title").isEqualTo("간식비10");
    }

    @DisplayName("2021년도 예산 내역만 가져온다.")
    @Test
    public void searchBudgetHistoryByYearTest() {

        //given
        List<BudgetHistory> histories = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            histories.add(
                    BudgetHistory.builder()
                            .title("간식비2000")
                            .details("과자2000")
                            .income(0)
                            .outcome(500000)
                            .dateUsed(LocalDateTime.of(2000 + (i / 15), 1, 1, 1, 1, 1))
                            .personReceived(received)
                            .personInCharge(inCharge)
                            .build());
        }
        budgetHistoryRepository.saveAll(histories);

        //when
        Page<BudgetHistoryDetailDto> page = budgetHistoryRepository.search(2001,
                PageRequest.of(0, 15, Direction.DESC, "dateUsed"));

        //then
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getNumberOfElements()).isEqualTo(6);
        assertThat(page.getTotalPages()).isEqualTo(1);
    }

    @DisplayName("회계내역이 있는 년도를 모두 가져온다.")
    @Test
    public void getAllYearOfHistory() {
        //given
        List<BudgetHistory> histories = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            histories.add(
                    BudgetHistory.builder()
                            .title("간식비2000")
                            .details("과자2000")
                            .income(0)
                            .outcome(500000)
                            .dateUsed(LocalDateTime.of(2000 + (i / 3), 1, 1, 1, 1, 1))
                            .personReceived(received)
                            .personInCharge(inCharge)
                            .build());
        }
        budgetHistoryRepository.saveAll(histories);

        //when
        List<Integer> allYear = budgetHistoryRepository.findAllYear();

        //then
        assertThat(allYear).containsExactly(2006, 2005, 2004, 2003, 2002, 2001, 2000);
        assertThat(allYear).hasSize(7);
    }

    @DisplayName("잔액을 구한다.")
    @Test
    public void getBalanceTest() {
        //given
        budgetHistoryRepository.save(
                BudgetHistory.builder()
                        .title("간식비")
                        .details("과자")
                        .income(0)
                        .outcome(500000)
                        .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                        .personReceived(received)
                        .personInCharge(inCharge)
                        .build());
        budgetHistoryRepository.save(
                BudgetHistory.builder()
                        .title("동아리 지원금")
                        .details("학사 지원")
                        .income(3000000)
                        .outcome(0)
                        .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                        .personReceived(inCharge)
                        .personInCharge(inCharge)
                        .build());
        budgetHistoryRepository.save(
                BudgetHistory.builder()
                        .title("강의 지원금")
                        .details("강의 결제 비용")
                        .income(0)
                        .outcome(200000)
                        .dateUsed(LocalDateTime.of(2000, 1, 1, 1, 1, 1))
                        .personReceived(received)
                        .personInCharge(inCharge)
                        .build());

        //when
        Integer balance = budgetHistoryRepository.getBalance();

        //then
        assertThat(balance).isEqualTo(2300000);
    }
}
