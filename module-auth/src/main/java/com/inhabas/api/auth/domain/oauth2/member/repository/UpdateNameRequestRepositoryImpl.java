package com.inhabas.api.auth.domain.oauth2.member.repository;

import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.QUpdateNameRequest.updateNameRequest;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.UpdateNameRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@Transactional
@RequiredArgsConstructor
public class UpdateNameRequestRepositoryImpl implements UpdateNameRequestRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private static final OrderSpecifier<Integer> ORDER_BY_STATUS =
      new CaseBuilder()
          .when(updateNameRequest.requestStatus.eq(PENDING))
          .then(1)
          .when(updateNameRequest.requestStatus.eq(APPROVED))
          .then(2)
          .when(updateNameRequest.requestStatus.eq(REJECTED))
          .then(3)
          .when(updateNameRequest.requestStatus.eq(COMPLETED))
          .then(4)
          .otherwise(5)
          .asc();

  @Override
  public List<UpdateNameRequest> findAllOrderByStatusAndDateRequested() {

    OrderSpecifier<LocalDateTime> orderByDateRequested = updateNameRequest.dateRequested.desc();

    return queryFactory
        .selectFrom(updateNameRequest)
        .orderBy(ORDER_BY_STATUS, orderByDateRequested)
        .fetch();
  }

  @Override
  public List<UpdateNameRequest> findAllByMemberIdOrderByStatusAndDateRequested(Long memberId) {
    OrderSpecifier<LocalDateTime> orderByDateRequested = updateNameRequest.dateRequested.desc();

    return queryFactory
        .selectFrom(updateNameRequest)
        .where(updateNameRequest.member.id.eq(memberId))
        .orderBy(ORDER_BY_STATUS, orderByDateRequested)
        .fetch();
  }
}
