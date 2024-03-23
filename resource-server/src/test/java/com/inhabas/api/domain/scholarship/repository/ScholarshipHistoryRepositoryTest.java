package com.inhabas.api.domain.scholarship.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.scholarship.domain.ScholarshipHistory;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.Data;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class ScholarshipHistoryRepositoryTest {

  @Autowired ScholarshipHistoryRepository scholarshipHistoryRepository;
  @Autowired MemberRepository memberRepository;
  @Autowired TestEntityManager em;

  @Test
  void getYearlyData() {
    // given
    Member writer = memberRepository.save(MemberTest.chiefMember());
    ScholarshipHistory scholarshipHistory =
        new ScholarshipHistory(writer, "title", LocalDateTime.now());
    ReflectionTestUtils.setField(scholarshipHistory, "id", 1L);
    ScholarshipHistory savedScholarshipHistory =
        scholarshipHistoryRepository.save(scholarshipHistory);
    Data data =
        new Data(1L, savedScholarshipHistory.getTitle(), savedScholarshipHistory.getDateHistory());
    List<YearlyData> savedData =
        List.of(new YearlyData(savedScholarshipHistory.getDateHistory().getYear(), List.of(data)));

    // when
    scholarshipHistoryRepository.save(scholarshipHistory);
    List<YearlyData> yearlyData = scholarshipHistoryRepository.getYearlyData();

    // then
    Assertions.assertThat(yearlyData.get(0).year).isEqualTo(savedData.get(0).year);
    Assertions.assertThat(yearlyData.get(0).data.get(0).id)
        .isEqualTo(savedData.get(0).data.get(0).id);
    Assertions.assertThat(yearlyData.get(0).data.get(0).dateHistory)
        .isEqualTo(savedData.get(0).data.get(0).dateHistory);
    Assertions.assertThat(yearlyData.get(0).data.get(0).title)
        .isEqualTo(savedData.get(0).data.get(0).title);
  }
}
