package com.inhabas.api.domain.board.repository;

import com.inhabas.api.domain.board.dto.BoardCountDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.inhabas.api.domain.board.domain.QBaseBoard.baseBoard;
import static com.inhabas.api.domain.menu.domain.QMenu.*;

import java.util.List;

@RequiredArgsConstructor
public class BaseBoardRepositoryImpl implements BaseBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardCountDto> countRowsGroupByMenuName(Integer menuGroupId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                BoardCountDto.class,
                                menu.name.value,
                                baseBoard.id.count().intValue()
                        ))
                .from(menu)
                .leftJoin(baseBoard).on(menu.id.eq(baseBoard.menu.id))
                .where(menu.menuGroup.id.eq(menuGroupId))
                .groupBy(menu.name)
                .fetch();
    }
}
