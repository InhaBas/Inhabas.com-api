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

  // 각 공모전 타입(공모전, 대외활동)에 따른 공모전 게시판, 대외활동 게시판 검색기능 각각 구현.
  // 예를 들어 contestType = EXTERNAL_ACTIVITY 이면 대외활동 게시판 검색기능.
  @Override
  public List<ContestBoard> findAllByContestBoardAndContestTypeLike(
      ContestType contestType, String search) {
    return queryFactory
        .selectFrom(contestBoard)
        .where(
            contestBoard
                .contestType
                .eq(contestType)
                .and(
                    titleLike(search)
                        .or(contentLike(search))
                        .or(writerNameLike(search))
                        .or(associationLike(search))
                        .or(topicLike(search))))
        .fetch();
  }

  private BooleanExpression titleLike(String search) {
    return contestBoard.title.value.likeIgnoreCase("%" + search + "%");
  }

  private BooleanExpression contentLike(String search) {
    return contestBoard.content.value.likeIgnoreCase("%" + search + "%");
  }

  private BooleanExpression writerNameLike(String search) {
    return contestBoard.writer.name.value.likeIgnoreCase("%" + search + "%");
  }

  private BooleanExpression associationLike(String search) {
    return contestBoard.association.value.likeIgnoreCase("%" + search + "%");
  }

  private BooleanExpression topicLike(String search) {
    return contestBoard.topic.value.likeIgnoreCase("%" + search + "%");
  }
}
