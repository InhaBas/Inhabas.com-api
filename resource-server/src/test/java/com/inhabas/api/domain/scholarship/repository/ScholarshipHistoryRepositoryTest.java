package com.inhabas.api.domain.scholarship.repository;

import static org.junit.jupiter.api.Assertions.*;

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
    Member writer = MemberTest.chiefMember();
    ReflectionTestUtils.setField(writer, "id", 1L);
    memberRepository.save(writer);
    ScholarshipHistory scholarshipHistory =
        new ScholarshipHistory(writer, "title", LocalDateTime.now());
    ReflectionTestUtils.setField(scholarshipHistory, "id", 1L);
    Data data = new Data(1L, scholarshipHistory.getTitle(), scholarshipHistory.getDateHistory());
    YearlyData savedData =
        new YearlyData(scholarshipHistory.getDateHistory().getYear(), List.of(data));

    // when
    scholarshipHistoryRepository.save(scholarshipHistory);
    List<YearlyData> yearlyData = scholarshipHistoryRepository.getYearlyData();

    // then
    Assertions.assertThat(yearlyData).contains(savedData);
  }
}
