package com.inhabas.api.domain.contest.repository;

import static com.inhabas.api.domain.contest.domain.QContestBoard.contestBoard;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Name;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ContestBoardRepositoryImpl implements ContestBoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  //    @Override
  //    public Optional<DetailContestBoardDto> findDtoById(Integer id) {
  //        return Optional.ofNullable(queryFactory
  //                .select(Projections.constructor(DetailContestBoardDto.class,
  //                            Expressions.asNumber(id).as("id"),
  //                            member.name.value,
  //                            contestBoard.title.value,
  //                            contestBoard.content.value,
  //                            contestBoard.association.value,
  //                            contestBoard.topic.value,
  //                            contestBoard.start,
  //                            contestBoard.deadline,
  //                            contestBoard.dateCreated,
  //                            contestBoard.dateUpdated
  //                        ))
  //                .from(contestBoard)
  //                .innerJoin(member).on(contestBoard.writerId.eq(member.studentId))
  //                .limit(1)
  //                .fetchOne());
  //    }

  // 보류
  @SuppressWarnings({"unchecked"})
  public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
    Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
    return new OrderSpecifier(order, fieldPath);
  }

  @SuppressWarnings({"unchecked"})
  private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
    List<OrderSpecifier> ORDERS = new ArrayList<>();
    if (pageable.getSort() != null) {
      for (Sort.Order order : pageable.getSort()) {
        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        switch (order.getProperty()) {
          case "id":
            OrderSpecifier<?> orderId = getSortedColumn(direction, contestBoard, "id");
            ORDERS.add(orderId);
            break;
          case "deadline":
            OrderSpecifier<?> orderDeadline = getSortedColumn(direction, contestBoard, "deadline");
            ORDERS.add(orderDeadline);
            break;
          default:
            break;
        }
      }
    } else {
      OrderSpecifier<?> orderDeadline = getSortedColumn(Order.DESC, contestBoard, "deadline");
      ORDERS.add(orderDeadline);
    }
    return ORDERS;
  }

  //    @Override
  //    public Page<ListContestBoardDto> findAllByMenuId(MenuId menuId, Pageable pageable) {
  //        List <OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
  //        List<ListContestBoardDto> results =
  // queryFactory.select(Projections.constructor(ListContestBoardDto.class,
  //                        contestBoard.title.value,
  //                        contestBoard.topic.value,
  //                        contestBoard.start,
  //                        contestBoard.deadline))
  //                .from(contestBoard)
  //                .where(menuEq(menuId))
  //                .offset(pageable.getOffset())
  //                .limit(pageable.getPageSize())
  //                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
  //                .fetch();
  //        return new PageImpl<>(results, pageable, results.size());
  //    }

  //    private BooleanExpression menuEq(MenuId menuId) {
  //        return contestBoard.menuId.eq(menuId);
  //    }

  // 공모전 게시판 검색
  @Override
  public List<ContestBoard> findAllByContestBoardLike(String search) {
    return queryFactory
        .selectFrom(contestBoard)
        .where(titleLike(search)
            .or(contentLike(search))
            .or(writerNameLike(search))
            .or(associationLike(search))
            .or(topicLike(search)))
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
