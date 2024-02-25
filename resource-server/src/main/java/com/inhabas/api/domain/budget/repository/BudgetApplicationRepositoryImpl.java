package com.inhabas.api.domain.budget.repository;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus.COMPLETED;
import static com.inhabas.api.domain.budget.domain.QBudgetSupportApplication.budgetSupportApplication;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class BudgetApplicationRepositoryImpl implements BudgetApplicationRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QMember applicant = new QMember("applicant");
  private final QMember memberInCharge = new QMember("memberInCharge");

  public BudgetApplicationRepositoryImpl(JPAQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  @Override
  public Optional<BudgetApplicationDetailDto> findDtoById(Long applicationId) {

    return Optional.ofNullable(
        getDtoJPAQuery().where(budgetSupportApplication.id.eq(applicationId)).fetchOne());
  }

  @Override
  public List<BudgetApplicationDto> search(RequestStatus status) {

    return queryFactory
        .select(
            Projections.constructor(
                BudgetApplicationDto.class,
                budgetSupportApplication.id,
                budgetSupportApplication.title.value,
                budgetSupportApplication.applicationWriter.id,
                applicant.name.value,
                budgetSupportApplication.dateCreated,
                budgetSupportApplication.status))
        .from(budgetSupportApplication)
        .innerJoin(applicant)
        .on(budgetSupportApplication.applicationWriter.eq(applicant))
        .where(sameStatus(status).and(budgetSupportApplication.status.ne(COMPLETED)))
        .orderBy(budgetSupportApplication.dateUsed.desc())
        .fetch();
  }

  private BooleanExpression sameStatus(RequestStatus status) {

    return status == null
        ? budgetSupportApplication.status.ne(COMPLETED)
        : budgetSupportApplication.status.eq(status);
  }

  // 쿼리 결과 EHCACHE 등으로 캐시할 필요가 있음. 카운트 쿼리가 비효율적임.
  private Integer getCount(RequestStatus status) {
    return queryFactory
        .selectFrom(budgetSupportApplication)
        .where(sameStatus(status))
        .fetch()
        .size();
  }

  private JPAQuery<BudgetApplicationDetailDto> getDtoJPAQuery() {
    return queryFactory
        .select(
            Projections.constructor(
                BudgetApplicationDetailDto.class,
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
                memberInCharge.name.value,
                budgetSupportApplication.status,
                budgetSupportApplication.rejectReason.value))
        .from(budgetSupportApplication)
        .innerJoin(applicant)
        .on(budgetSupportApplication.applicationWriter.eq(applicant))
        .leftJoin(memberInCharge)
        .on(
            budgetSupportApplication.personInCharge.eq(
                memberInCharge)); // 총무가 아직 승인 또는 거절 안했을수 있기 때문에 null 일 수 있다.
  }
}
