package com.inhabas.api.domain.scholarship.repository;

import static com.inhabas.api.domain.scholarship.domain.QScholarship.scholarship;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.scholarship.domain.Scholarship;
import com.inhabas.api.domain.scholarship.domain.ScholarshipBoardType;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ScholarshipBoardRepositoryImpl implements ScholarshipBoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<ScholarshipBoardDto> findAllByTypeAndSearch(
      ScholarshipBoardType boardType, String search) {
    return queryFactory
        .select(
            Projections.constructor(
                ScholarshipBoardDto.class,
                scholarship.id,
                scholarship.title.value,
                scholarship.writer,
                scholarship.dateCreated,
                scholarship.dateUpdated))
        .from(scholarship)
        .where(eqScholarshipBoardType(boardType))
        .orderBy(scholarship.dateCreated.desc())
        .fetch();
  }

  @Override
  public Optional<Scholarship> findByTypeAndId(ScholarshipBoardType boardType, Long boardId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(scholarship)
            .where((eqScholarshipBoardType(boardType)).and(scholarship.id.eq(boardId)))
            .orderBy(scholarship.dateCreated.desc())
            .fetchOne());
  }

  private BooleanExpression eqScholarshipBoardType(ScholarshipBoardType scholarshipBoardType) {
    return scholarship.menu.id.eq(scholarshipBoardType.getMenuId());
  }
}
