package com.inhabas.api.domain.scholarship.repository;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.scholarship.domain.QScholarshipHistory;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ScholarshipHistoryRepositoryImpl {

  private final JPAQueryFactory queryFactory;
  private QScholarshipHistory scholarshipHistory = QScholarshipHistory.scholarshipHistory;

  public List<YearlyContents> getYearlyContents() {
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
                tuple -> tuple.get(scholarshipHistory.dateHistory).getYear(),
                Collectors.mapping(
                    tuple ->
                        new Content(
                            tuple.get(scholarshipHistory.dateHistory),
                            tuple.get(scholarshipHistory.title)),
                    Collectors.toList())))
        .entrySet()
        .stream()
        .map(entry -> new YearlyContents(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  // 연도별 컨텐츠를 담는 클래스
  public static class YearlyContents {
    public int year;
    public List<Content> contents;

    public YearlyContents(int year, List<Content> contents) {
      this.year = year;
      this.contents = contents;
    }
  }

  // 날짜와 컨텐츠 정보를 담는 클래스
  public static class Content {
    public String date;
    public String content;

    public Content(String date, String content) {
      this.date = date;
      this.content = content;
    }
  }
}
