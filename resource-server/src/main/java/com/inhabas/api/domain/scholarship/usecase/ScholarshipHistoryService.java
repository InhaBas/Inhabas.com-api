package com.inhabas.api.domain.scholarship.usecase;

import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;
import java.util.List;

import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;

public interface ScholarshipHistoryService {

  List<YearlyData> getScholarshipHistories();

  Long writeScholarshipHistory(Long memberId, SaveScholarshipHistoryDto saveScholarshipHistoryDto);

  void updateScholarshipHistory(
      Long memberId,
      Long scholarshipHistoryId,
      SaveScholarshipHistoryDto saveScholarshipHistoryDto);

  void deleteScholarshipHistory(Long scholarshipHistoryId);
}
