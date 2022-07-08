package com.inhabas.api.domain.budget.repository;

import static com.inhabas.api.domain.budget.domain.QBudgetHistory.budgetHistory;

import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.member.domain.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class BudgetHistoryRepositoryImpl implements BudgetHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BudgetHistoryDetailDto> findAllByPageable(Pageable pageable) {

        QMember memberInCharge = new QMember("inCharge");
        QMember memberReceived = new QMember("received");

        List<BudgetHistoryDetailDto> result = queryFactory.select(
                        Projections.constructor(BudgetHistoryDetailDto.class,
                                budgetHistory.dateUsed,
                                budgetHistory.created,
                                budgetHistory.updated,
                                budgetHistory.title,
                                budgetHistory.income,
                                budgetHistory.outcome,
                                budgetHistory.details,
                                budgetHistory.personReceived.id,
                                memberReceived.name.value,
                                budgetHistory.personInCharge.id,
                                memberInCharge.name.value
                                ))
                .from(budgetHistory)
                .innerJoin(memberInCharge).on(memberInCharge.id.id.eq(budgetHistory.personInCharge.id))
                .innerJoin(memberReceived).on(memberReceived.id.id.eq(budgetHistory.personReceived.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy()
                .fetch();

        return new PageImpl<>(result, pageable, getCount());
    }

    // need to cache such as EHCACHE later.
    private Integer getCount() {
        return queryFactory.selectFrom(budgetHistory).fetch().size();
    }
}
