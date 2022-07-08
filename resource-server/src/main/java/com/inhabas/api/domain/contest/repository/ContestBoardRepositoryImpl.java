package com.inhabas.api.domain.contest.repository;


import static com.inhabas.api.domain.contest.domain.QContestBoard.contestBoard;
import static com.inhabas.api.domain.member.domain.entity.QMember.member;

import com.inhabas.api.domain.contest.dto.DetailContestBoardDto;
import com.inhabas.api.domain.contest.dto.ListContestBoardDto;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class ContestBoardRepositoryImpl implements ContestBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<DetailContestBoardDto> findDtoById(Integer id) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(DetailContestBoardDto.class,
                            Expressions.asNumber(id).as("id"),
                            member.name.value,
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
                .innerJoin(member).on(contestBoard.writerId.eq(member.id))
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
    public Page<ListContestBoardDto> findAllByMenuId(MenuId menuId, Pageable pageable) {
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

    private BooleanExpression menuEq(MenuId menuId) {
        return contestBoard.menuId.eq(menuId);
    }

}