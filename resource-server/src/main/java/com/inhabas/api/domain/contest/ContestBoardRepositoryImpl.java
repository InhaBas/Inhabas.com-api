package com.inhabas.api.domain.contest;


import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.contest.QContestBoard.contestBoard;

@RequiredArgsConstructor
public class ContestBoardRepositoryImpl implements ContestBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<DetailContestBoardDto> findDtoById(Integer id) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(DetailContestBoardDto.class,
                            Expressions.asNumber(id).as("id"),
                            contestBoard.writer.name.value,
                            contestBoard.title.value,
                            contestBoard.contents.value,
                            contestBoard.association.value,
                            contestBoard.topic.value,
                            contestBoard.start,
                            contestBoard.deadline,
                            contestBoard.created,
                            contestBoard.updated
                        ))
                .from(contestBoard)
                .innerJoin(contestBoard.writer).on(contestBoard.id.eq(id))
                .limit(1)
                .fetchOne());
    }

    public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName){
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        if (pageable.getSort() != null){
            for (Sort.Order order : pageable.getSort()){
                Order direction = order.getDirection().isAscending()
                        ? Order.ASC : Order.DESC;
                    switch(order.getProperty()){
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

    @Override
    public Page<ListContestBoardDto> findAllByMenuId(Integer menuId, Pageable pageable) {
        List <OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        List<ListContestBoardDto> results = queryFactory.select(Projections.constructor(ListContestBoardDto.class,
                        contestBoard.title.value,
                        contestBoard.topic.value,
                        contestBoard.start,
                        contestBoard.deadline))
                .from(contestBoard)
                .where(menuEq(menuId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .fetch();
        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanExpression menuEq(Integer menuId) {
        return contestBoard.menu.id.eq(menuId);
    }

}