package com.inhabas.api.domain.budget.repository;

import static com.inhabas.api.domain.budget.domain.QBudgetHistory.budgetHistory;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class BudgetHistoryRepositoryImpl implements BudgetHistoryRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public BudgetHistoryRepositoryImpl(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Override
  public Optional<BudgetHistoryDetailDto> findDtoById(Long id) {

    return Optional.ofNullable(getDtoJPAQuery().where(budgetHistory.id.eq(id)).fetchOne());
  }

  @Override
  public List<BudgetHistoryDetailDto> search(Integer year) {

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

  private JPAQuery<BudgetHistoryDetailDto> getDtoJPAQuery() {

    return queryFactory
        .select(
            Projections.constructor(
                BudgetHistoryDetailDto.class,
                budgetHistory.id,
                budgetHistory.dateUsed,
                budgetHistory.dateCreated,
                budgetHistory.dateUpdated,
                budgetHistory.title.value,
                budgetHistory.details.value,
                budgetHistory.income.value,
                budgetHistory.outcome.value,
                budgetHistory.account.value,
                budgetHistory.memberReceived.studentId.id,
                budgetHistory.memberReceived.name.value,
                budgetHistory.memberInCharge.studentId.id,
                budgetHistory.memberInCharge.name.value))
        .from(budgetHistory);
  }
}
