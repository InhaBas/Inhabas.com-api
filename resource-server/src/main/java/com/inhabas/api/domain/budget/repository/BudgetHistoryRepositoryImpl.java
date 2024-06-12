package com.inhabas.api.domain.budget.repository;

import static com.inhabas.api.domain.budget.domain.QBudgetHistory.budgetHistory;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class BudgetHistoryRepositoryImpl implements BudgetHistoryRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<BudgetHistoryDto> search(Integer year) {

    return getDtoJPAQuery().where(createdIn(year)).orderBy(budgetHistory.dateUsed.desc()).fetch();
  }

  @Override
  public List<Integer> findAllYear() {
    return queryFactory
        .select(budgetHistory.dateUsed.year())
        .from(budgetHistory)
        .distinct()
        .orderBy(budgetHistory.dateUsed.desc())
        .fetch();
  }

  private BooleanExpression createdIn(Integer year) {
    return year == null
        ? Expressions.asBoolean(true).isTrue()
        : budgetHistory.dateUsed.year().eq(year);
  }

  // need to cache such as EHCACHE later.
  private Integer getCount(Integer year) {
    return queryFactory.selectFrom(budgetHistory).where(createdIn(year)).fetch().size();
  }

  private JPAQuery<BudgetHistoryDto> getDtoJPAQuery() {

    return queryFactory
        .select(
            Projections.constructor(
                BudgetHistoryDto.class,
                budgetHistory.id,
                budgetHistory.dateUsed,
                budgetHistory.dateCreated,
                budgetHistory.dateUpdated,
                budgetHistory.title.value,
                budgetHistory.income.value,
                budgetHistory.outcome.value,
                budgetHistory.memberReceived.studentId.id,
                budgetHistory.memberReceived.name.value,
                budgetHistory.memberInCharge.id,
                budgetHistory.memberInCharge.studentId.id,
                budgetHistory.memberInCharge.name.value))
        .from(budgetHistory);
  }
}
