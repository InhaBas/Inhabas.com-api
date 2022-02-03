package com.inhabas.api.domain.contest;


import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @Override
    public Page<ListContestBoardDto> findAllByMenuId(Integer menuId, Pageable pageable) {
        List<ListContestBoardDto> results = queryFactory.select(Projections.constructor(ListContestBoardDto.class,
                            contestBoard.title.value,
                            contestBoard.topic.value,
                            contestBoard.start,
                            contestBoard.deadline))
                .from(contestBoard)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(results, pageable, results.size());
    }
}
