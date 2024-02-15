package com.inhabas.api.domain.contest.repository;

import static com.inhabas.api.domain.contest.domain.QContestBoard.contestBoard;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ContestBoardRepositoryImpl implements ContestBoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  // 보류
  @SuppressWarnings({"unchecked"})
  public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
    Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
    return new OrderSpecifier(order, fieldPath);
  }

  // 공모전 게시판 정렬 기능
  @SuppressWarnings({"unchecked"})
  private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
    List<OrderSpecifier<?>> ORDERS = new ArrayList<>();
    if (pageable.getSort() != null) {
      for (Sort.Order order : pageable.getSort()) {
        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        switch (order.getProperty()) {
          case "id":
            OrderSpecifier<?> orderId = getSortedColumn(direction, contestBoard, "id");
            ORDERS.add(orderId);
            break;
          case "dateContestEnd":
            OrderSpecifier<?> orderDateContestEnd =
                getSortedColumn(direction, contestBoard, "dateContestEnd");
            ORDERS.add(orderDateContestEnd);
            break;
          default:
            break;
        }
      }
    } else {
      OrderSpecifier<?> orderDateContestEnd =
          getSortedColumn(Order.DESC, contestBoard, "dateContestEnd");
      ORDERS.add(orderDateContestEnd);
    }
    return ORDERS;
  }

  // 공모전 검색 및 필터링 기능
  public List<ContestBoard> findAllByContestTypeAndFieldLike(
      ContestType contestType, Long contestFieldId, String search) {
    BooleanExpression target =
        contestTypeEq(contestType)
            .and(contestFieldEq(contestFieldId))
            .and(
                titleLike(search)
                    .or(contentLike(search))
                    .or(writerNameLike(search))
                    .or(associationLike(search))
                    .or(topicLike(search)));

    return queryFactory.selectFrom(contestBoard).where(target).fetch();
  }

  private BooleanExpression contestTypeEq(ContestType contestType) {
    return contestBoard.contestType.eq(contestType);
  }

  private BooleanExpression contestFieldEq(Long contestField) {
    if (contestField == null) {
      return null;
    }
    return contestBoard.contestField.id.eq(contestField);
  }

  private BooleanExpression titleLike(String search) {
    return hasText(search) ? contestBoard.title.value.containsIgnoreCase(search) : null;
  }

  private BooleanExpression contentLike(String search) {
    return hasText(search) ? contestBoard.content.value.containsIgnoreCase(search) : null;
  }

  private BooleanExpression writerNameLike(String search) {
    return hasText(search) ? contestBoard.writer.name.value.containsIgnoreCase(search) : null;
  }

  private BooleanExpression associationLike(String search) {
    return hasText(search) ? contestBoard.association.value.containsIgnoreCase(search) : null;
  }

  private BooleanExpression topicLike(String search) {
    return hasText(search) ? contestBoard.topic.value.containsIgnoreCase(search) : null;
  }

  private Boolean hasText(String text) {
    return text != null && !text.trim().isEmpty();
  }
}
