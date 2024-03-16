package com.inhabas.api.domain.scholarship.usecase;

import java.util.List;

import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;

public interface ScholarshipHistoryService {

  List<YearlyData> getScholarshipHistories();

  Long writeScholarshipHistory(Long memberId, SaveScholarshipHistoryDto saveScholarshipHistoryDto);

  void updateScholarshipHistory(
      Long memberId,
      Long scholarshipHistoryId,
      SaveScholarshipHistoryDto saveScholarshipHistoryDto);

  void deleteScholarshipHistory(Long scholarshipHistoryId);
}
