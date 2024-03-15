package com.inhabas.api.domain.scholarship.repository;

import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;
import java.util.List;

public interface ScholarshipHistoryRepositoryCustom {
  List<YearlyData> getYearlyData();
}
