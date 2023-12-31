package com.inhabas.api.domain.board.repository;

import static com.inhabas.api.domain.board.domain.QNormalBoard.normalBoard;
import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.QMember.member;

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
                        Expressions.asString("").as("content"),
                        member.name.value,
                        normalBoard.menuId,
                        normalBoard.dateCreated,
                        normalBoard.dateUpdated))
                .from(normalBoard)
                .innerJoin(member).on(eqMemberId())
                .where(eqMenuId(menuId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(normalBoard.dateCreated.desc())
                .fetch();

        return new PageImpl<>(results, pageable, this.getCount(menuId));
    }

    private BooleanExpression eqMenuId(MenuId menuId) {
        return normalBoard.menuId.eq(menuId);
    }

    // 캐시 필요함.
    private Integer getCount(MenuId menuId) {
        return queryFactory.selectFrom(normalBoard)
                .where(eqMenuId(menuId))
                .fetch()
                .size();
    }

    @Override
    public Optional<BoardDto> findDtoById(Integer id) {

        BoardDto target = queryFactory.select(Projections.constructor(BoardDto.class,
                        Expressions.asNumber(id).as("id"),
                        normalBoard.title.value,
                        normalBoard.contents.value,
                        member.name.value,
                        normalBoard.menuId,
                        normalBoard.dateCreated,
                        normalBoard.dateUpdated))
                .from(normalBoard)
                .innerJoin(member).on(eqMemberId())
                .where(normalBoard.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(target);
    }

    private BooleanExpression eqMemberId() {
        return normalBoard.writerId.eq(member.studentId);
    }
}