package com.inhabas.api.domain.scholarship.repository;

import static com.inhabas.api.domain.scholarship.domain.QScholarshipHistory.scholarshipHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.scholarship.domain.ScholarshipHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ScholarshipHistoryRepositoryImpl implements ScholarshipHistoryRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<YearlyData> getYearlyData() {

    List<ScholarshipHistory> histories =
        queryFactory
            .selectFrom(scholarshipHistory)
            .orderBy(
                scholarshipHistory.dateHistory.year().asc(), scholarshipHistory.dateHistory.asc())
            .fetch();

    return histories.stream()
        .collect(
            Collectors.groupingBy(
                history -> history.getDateHistory().getYear(),
                Collectors.mapping(
                    history ->
                        new Data(history.getId(), history.getTitle(), history.getDateHistory()),
                    Collectors.toList())))
        .entrySet()
        .stream()
        .map(entry -> new YearlyData(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  // 연도별 컨텐츠를 담는 클래스
  public static class YearlyData {

    public int year;
    public List<Data> data;

    public YearlyData(int year, List<Data> data) {
      this.year = year;
      this.data = data;
    }
  }

  public static class Data {

    public Long id;
    public String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime dateHistory;

    public Data(Long id, String title, LocalDateTime dateHistory) {
      this.id = id;
      this.title = title;
      this.dateHistory = dateHistory;
    }
  }
}
