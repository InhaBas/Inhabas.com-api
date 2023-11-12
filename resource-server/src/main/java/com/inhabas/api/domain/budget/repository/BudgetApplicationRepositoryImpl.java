package com.inhabas.api.domain.budget.repository;

import static com.inhabas.api.domain.budget.domain.QBudgetSupportApplication.budgetSupportApplication;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember;
import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class BudgetApplicationRepositoryImpl implements BudgetApplicationRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QMember applicant = new QMember("applicant");
    private final QMember pic = new QMember("pic");

    public BudgetApplicationRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<BudgetApplicationDetailDto> findDtoById(Integer applicationId) {

        return Optional.ofNullable(
                getDtoJPAQuery()
                        .where(budgetSupportApplication.id.eq(applicationId))
                        .fetchOne()
        );
    }

    @Override
    public Page<BudgetApplicationListDto> search(ApplicationStatus status, Pageable pageable) {

        List<BudgetApplicationListDto> result =
                queryFactory.select(
                        Projections.constructor(BudgetApplicationListDto.class,
                                budgetSupportApplication.id,
                                budgetSupportApplication.title.value,
                                budgetSupportApplication.applicationWriter.id,
                                applicant.name.value,
                                budgetSupportApplication.dateCreated,
                                budgetSupportApplication.status
                        ))
                .from(budgetSupportApplication)
                .innerJoin(applicant).on(budgetSupportApplication.applicationWriter.eq(applicant.studentId))
                .where(sameStatus(status).and(budgetSupportApplication.status.ne(ApplicationStatus.PROCESSED)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(budgetSupportApplication.dateUsed.desc())
                .fetch();

        return new PageImpl<>(result, pageable, getCount(status));
    }

    private BooleanExpression sameStatus(ApplicationStatus status) {

        return status == null ? budgetSupportApplication.status.ne(ApplicationStatus.PROCESSED)
                : budgetSupportApplication.status.eq(status);
    }

    // 쿼리 결과 EHCACHE 등으로 캐시할 필요가 있음. 카운트 쿼리가 비효율적임.
    private Integer getCount(ApplicationStatus status) {
        return queryFactory
                .selectFrom(budgetSupportApplication)
                .where(sameStatus(status))
                .fetch().size();
    }

    private JPAQuery<BudgetApplicationDetailDto> getDtoJPAQuery() {
        return queryFactory.select(
                        Projections.constructor(BudgetApplicationDetailDto.class,
                                budgetSupportApplication.id,
                                budgetSupportApplication.title.value,
                                budgetSupportApplication.dateUsed,
                                budgetSupportApplication.dateCreated,
                                budgetSupportApplication.details.value,
                                budgetSupportApplication.outcome.value,
                                budgetSupportApplication.applicantAccount.value,
                                budgetSupportApplication.applicationWriter.id,
                                applicant.name.value,
                                budgetSupportApplication.personInCharge.id,
                                pic.name.value,
                                budgetSupportApplication.status,
                                budgetSupportApplication.rejectReason.value
                        ))
                .from(budgetSupportApplication)
                .innerJoin(applicant).on(budgetSupportApplication.applicationWriter.eq(applicant.studentId))
                .leftJoin(pic).on(budgetSupportApplication.personInCharge.eq(pic.studentId)); // 총무가 아직 승인 또는 거절 안했을수 있기 때문에 null 일 수 있다.
    }
}
