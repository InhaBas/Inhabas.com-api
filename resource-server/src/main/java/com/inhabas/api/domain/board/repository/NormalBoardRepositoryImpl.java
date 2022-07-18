package com.inhabas.api.domain.board.repository;

import static com.inhabas.api.domain.board.domain.QNormalBoard.normalBoard;
import static com.inhabas.api.domain.member.domain.entity.QMember.member;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.board.dto.BoardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class NormalBoardRepositoryImpl implements NormalBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardDto> findAllByMenuId(MenuId menuId, Pageable pageable) {
        List<BoardDto> results = queryFactory.select(Projections.constructor(BoardDto.class,
                        normalBoard.id,
                        normalBoard.title.value,
                        Expressions.asString("").as("contents"),
                        member.name.value,
                        normalBoard.menuId,
                        normalBoard.created,
                        normalBoard.updated))
                .from(normalBoard)
                .innerJoin(member).on(eqMemberId())
                .where(menuEq(menuId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanExpression menuEq(MenuId menuId) {
        return normalBoard.menuId.eq(menuId);
    }

    @Override
    public Optional<BoardDto> findDtoById(Integer id) {

        BoardDto target = queryFactory.select(Projections.constructor(BoardDto.class,
                        Expressions.asNumber(id).as("id"),
                        normalBoard.title.value,
                        normalBoard.contents.value,
                        member.name.value,
                        normalBoard.menuId,
                        normalBoard.created,
                        normalBoard.updated))
                .from(normalBoard)
                .innerJoin(member).on(eqMemberId())
                .where(normalBoard.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(target);
    }

    private BooleanExpression eqMemberId() {
        return normalBoard.writerId.eq(member.id);
    }
}