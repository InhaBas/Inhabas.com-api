package com.inhabas.api.domain.budget.repository;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.member.domain.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.budget.domain.QBudgetHistory.budgetHistory;

public class BudgetHistoryRepositoryImpl implements BudgetHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QMember memberInCharge = new QMember("inCharge");
    private final QMember memberReceived = new QMember("received");


    public BudgetHistoryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public Optional<BudgetHistoryDetailDto> findDtoById(Integer id) {

        return Optional.ofNullable(
                getDtoJPAQuery()
                .where(budgetHistory.id.eq(id))
                .fetchOne());
    }


    @Override
    public Page<BudgetHistoryDetailDto> search(Integer year, Pageable pageable) {

        List<BudgetHistoryDetailDto> result = getDtoJPAQuery()
                .where(createdIn(year))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(budgetHistory.dateUsed.desc())
                .fetch();

        return new PageImpl<>(result, pageable, getCount(year));
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
        return year == null ?
                Expressions.asBoolean(true).isTrue()
                : budgetHistory.dateUsed.year().eq(year);
    }


    // need to cache such as EHCACHE later.
    private Integer getCount(Integer year) {
        return queryFactory.selectFrom(budgetHistory)
                .where(createdIn(year))
                .fetch()
                .size();
    }


    private JPAQuery<BudgetHistoryDetailDto> getDtoJPAQuery() {

        return queryFactory.select(
                        Projections.constructor(BudgetHistoryDetailDto.class,
                                budgetHistory.id,
                                budgetHistory.dateUsed,
                                budgetHistory.created,
                                budgetHistory.updated,
                                budgetHistory.title,
                                budgetHistory.income,
                                budgetHistory.outcome,
                                budgetHistory.details,
                                budgetHistory.account,
                                budgetHistory.personReceived.id,
                                memberReceived.name.value,
                                budgetHistory.personInCharge.id,
                                memberInCharge.name.value
                        ))
                .from(budgetHistory)
                .innerJoin(memberInCharge).on(memberInCharge.id.id.eq(budgetHistory.personInCharge.id))
                .innerJoin(memberReceived).on(memberReceived.id.id.eq(budgetHistory.personReceived.id));
    }
}
