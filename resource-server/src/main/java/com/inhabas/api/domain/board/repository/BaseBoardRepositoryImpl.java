package com.inhabas.api.domain.board.repository;

import com.inhabas.api.domain.board.dto.BoardCountDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.inhabas.api.domain.board.domain.QBaseBoard.baseBoard;
import java.util.List;

@RequiredArgsConstructor
public class BaseBoardRepositoryImpl implements BaseBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardCountDto> countRowsGroupByMenuId() {
        return queryFactory
                .select(
                        Projections.constructor(
                                BoardCountDto.class,
                                baseBoard.menu.id,
                                baseBoard.id.count()
                        ))
                .from(baseBoard)
                .groupBy(baseBoard.menu.id)
                .fetch();
    }
}
