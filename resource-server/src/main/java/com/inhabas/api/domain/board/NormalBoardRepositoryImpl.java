package com.inhabas.api.domain.board;

import com.inhabas.api.dto.board.BoardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.board.QNormalBoard.normalBoard;

@RequiredArgsConstructor
public class NormalBoardRepositoryImpl implements NormalBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardDto> findAllByMenuId(Integer menuId, Pageable pageable) {
        List<BoardDto> results = queryFactory.select(Projections.constructor(BoardDto.class,
                        normalBoard.id,
                        normalBoard.title.value,
                        Expressions.asString("").as("contents"),
                        normalBoard.writer.name.value,
                        Expressions.asNumber(menuId).as("menuId"),
                        normalBoard.created,
                        normalBoard.updated))
                .from(normalBoard)
                .innerJoin(normalBoard.writer)
                .where(menuEq(menuId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanExpression menuEq(Integer categoryId) {
        return normalBoard.menu.id.eq(categoryId);
    }

    @Override
    public Optional<BoardDto> findDtoById(Integer id) {

        BoardDto target = queryFactory.select(Projections.constructor(BoardDto.class,
                        Expressions.asNumber(id).as("id"),
                        normalBoard.title.value,
                        normalBoard.contents.value,
                        normalBoard.writer.name.value,
                        normalBoard.menu.id,
                        normalBoard.created,
                        normalBoard.updated))
                .from(normalBoard)
                .innerJoin(normalBoard.writer).on(normalBoard.id.eq(id))
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(target);
    }
}