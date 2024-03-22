package com.inhabas.api.domain.scholarship.usecase;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.scholarship.domain.ScholarshipHistory;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepository;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;

@Service
@RequiredArgsConstructor
public class ScholarshipHistoryServiceImpl implements ScholarshipHistoryService {

  private final ScholarshipHistoryRepository scholarshipHistoryRepository;
  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  @Override
  public List<YearlyData> getScholarshipHistories() {

    return scholarshipHistoryRepository.getYearlyData();
  }

  @Transactional
  @Override
  public Long writeScholarshipHistory(
      Long memberId, SaveScholarshipHistoryDto saveScholarshipHistoryDto) {
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    ScholarshipHistory scholarshipHistory =
        ScholarshipHistory.builder()
            .writer(writer)
            .title(saveScholarshipHistoryDto.getTitle())
            .dateHistory(saveScholarshipHistoryDto.getDateHistory())
            .build();

    return scholarshipHistoryRepository.save(scholarshipHistory).getId();
  }

  @Transactional
  @Override
  public void updateScholarshipHistory(
      Long memberId,
      Long scholarshipHistoryId,
      SaveScholarshipHistoryDto saveScholarshipHistoryDto) {
    ScholarshipHistory scholarshipHistory =
        scholarshipHistoryRepository
            .findById(scholarshipHistoryId)
            .orElseThrow(NotFoundException::new);
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    scholarshipHistory.update(writer, saveScholarshipHistoryDto);
  }

  @Transactional
  @Override
  public void deleteScholarshipHistory(Long scholarshipHistoryId) {
    ScholarshipHistory scholarshipHistory =
        scholarshipHistoryRepository
            .findById(scholarshipHistoryId)
            .orElseThrow(NotFoundException::new);
    scholarshipHistoryRepository.delete(scholarshipHistory);
  }
}
