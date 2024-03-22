package com.inhabas.api.domain.scholarship.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.scholarship.domain.ScholarshipHistory;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepository;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.Data;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class ScholarshipHistoryServiceImplTest {

  @InjectMocks ScholarshipHistoryServiceImpl scholarshipHistoryService;
  @Mock ScholarshipHistoryRepository scholarshipHistoryRepository;
  @Mock MemberRepository memberRepository;

  @DisplayName("장학회 연혁 조회 성공")
  @Test
  void getScholarshipHistories() {
    // given
    YearlyData yearlyData =
        new YearlyData(2023, List.of(new Data(1L, "title", LocalDateTime.now())));
    List<YearlyData> yearlyDataList = List.of(yearlyData);
    given(scholarshipHistoryRepository.getYearlyData()).willReturn(yearlyDataList);

    // when
    List<YearlyData> result = scholarshipHistoryService.getScholarshipHistories();

    // then
    then(scholarshipHistoryRepository).should().getYearlyData();
    assertThat(result).hasSize(1);
  }

  @DisplayName("장학회 연혁 생성 성공")
  @Test
  void writeScholarshipHistory() {
    // given
    Long memberId = 1L;
    Member member = MemberTest.chiefMember(); // 필요한 속성으로 Member 객체 초기화
    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        new SaveScholarshipHistoryDto("title", LocalDateTime.now());

    given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
    given(scholarshipHistoryRepository.save(any(ScholarshipHistory.class)))
        .willAnswer(
            invocation -> {
              ScholarshipHistory scholarshipHistory = invocation.getArgument(0);
              ReflectionTestUtils.setField(scholarshipHistory, "id", 1L);
              return scholarshipHistory;
            });

    // when
    Long resultId =
        scholarshipHistoryService.writeScholarshipHistory(memberId, saveScholarshipHistoryDto);

    // then
    then(memberRepository).should().findById(memberId);
    then(scholarshipHistoryRepository).should().save(any(ScholarshipHistory.class));
    assertThat(resultId).isEqualTo(1L);
  }

  @DisplayName("장학회 연혁 수정 성공")
  @Test
  void updateScholarshipHistory() {
    // given
    Member member = MemberTest.chiefMember();
    ScholarshipHistory scholarshipHistory =
        ScholarshipHistory.builder()
            .writer(member)
            .title("title")
            .dateHistory(LocalDateTime.now())
            .build();
    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        new SaveScholarshipHistoryDto("title", LocalDateTime.now());
    given(scholarshipHistoryRepository.findById(any()))
        .willReturn(Optional.ofNullable(scholarshipHistory));
    given(memberRepository.findById(any())).willReturn(Optional.of(member));

    // when
    scholarshipHistoryService.updateScholarshipHistory(1L, 1L, saveScholarshipHistoryDto);

    // then
    then(scholarshipHistoryRepository).should().findById(any());
    then(memberRepository).should().findById(any());
  }

  @DisplayName("장학회 연혁 삭제 성공")
  @Test
  void deleteScholarshipHistory() {
    // given
    ScholarshipHistory scholarshipHistory =
        ScholarshipHistory.builder()
            .writer(MemberTest.chiefMember())
            .title("title")
            .dateHistory(LocalDateTime.now())
            .build();
    given(scholarshipHistoryRepository.findById(any()))
        .willReturn(Optional.ofNullable(scholarshipHistory));

    // when
    scholarshipHistoryService.deleteScholarshipHistory(any());

    // then
    then(scholarshipHistoryRepository).should().findById(any());
    then(scholarshipHistoryRepository).should().delete(any());
  }
}
