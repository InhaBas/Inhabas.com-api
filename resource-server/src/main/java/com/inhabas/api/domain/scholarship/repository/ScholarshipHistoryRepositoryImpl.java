package com.inhabas.api.domain.scholarship.repository;

import static com.inhabas.api.domain.scholarship.domain.QScholarshipHistory.scholarshipHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ScholarshipHistoryRepositoryImpl implements ScholarshipHistoryRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<YearlyData> getYearlyData() {

    List<Tuple> results =
        queryFactory
            .select(scholarshipHistory.dateHistory, scholarshipHistory.title)
            .from(scholarshipHistory)
            .orderBy(
                scholarshipHistory.dateHistory.year().asc(), scholarshipHistory.dateHistory.asc())
            .fetch();

    return results.stream()
        .collect(
            Collectors.groupingBy(
                tuple ->
                    Objects.requireNonNull(tuple.get(scholarshipHistory.dateHistory)).getYear(),
                Collectors.mapping(
                    tuple ->
                        new Data(
                            tuple.get(scholarshipHistory.id),
                            tuple.get(scholarshipHistory.title.value),
                            tuple.get(scholarshipHistory.dateHistory)),
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
    public LocalDateTime dateHistory;

    public Data(Long id, String title, LocalDateTime dateHistory) {
      this.id = id;
      this.title = title;
      this.dateHistory = dateHistory;
    }
  }
}
