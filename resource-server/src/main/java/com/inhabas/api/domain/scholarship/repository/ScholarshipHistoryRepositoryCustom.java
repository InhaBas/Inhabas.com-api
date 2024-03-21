package com.inhabas.api.domain.scholarship.repository;

import java.util.List;

import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;

public interface ScholarshipHistoryRepositoryCustom {
  List<YearlyData> getYearlyData();
}
