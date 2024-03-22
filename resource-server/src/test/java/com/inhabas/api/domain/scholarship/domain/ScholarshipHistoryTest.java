package com.inhabas.api.domain.scholarship.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScholarshipHistoryTest {

  @DisplayName("Dto 를 사용해서 scholarshipHistory를 수정한다.")
  @Test
  void update() {
    // given
    Member writer = MemberTest.chiefMember();
    ScholarshipHistory scholarshipHistory =
        ScholarshipHistory.builder()
            .writer(writer)
            .title("oldTitle")
            .dateHistory(LocalDateTime.now())
            .build();

    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        SaveScholarshipHistoryDto.builder().title("title").dateHistory(LocalDateTime.now()).build();

    // when
    scholarshipHistory.update(writer, saveScholarshipHistoryDto);

    // then
    assertThat(scholarshipHistory)
        .extracting(
            ScholarshipHistory -> scholarshipHistory.getTitle(),
            ScholarshipHistory -> scholarshipHistory.getDateHistory())
        .containsExactly(
            saveScholarshipHistoryDto.getTitle(), saveScholarshipHistoryDto.getDateHistory());
  }
}
