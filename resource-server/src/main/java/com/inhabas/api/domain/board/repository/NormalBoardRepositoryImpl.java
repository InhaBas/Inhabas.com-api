package com.inhabas.api.domain.board.repository;

import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.domain.NormalBoardType;
import com.inhabas.api.domain.board.dto.NormalBoardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.board.domain.QNormalBoard.normalBoard;

@RequiredArgsConstructor
public class NormalBoardRepositoryImpl implements NormalBoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<NormalBoardDto> findAllByMemberIdAndTypeAndSearch(Long memberId, NormalBoardType boardType, String search) {
    return queryFactory
            .select(
                    Projections.constructor(
                            NormalBoardDto.class,
                            normalBoard.id,
                            normalBoard.title.value,
                            normalBoard.writer.name.value,
                            normalBoard.dateCreated,
                            normalBoard.dateUpdated,
                            normalBoard.isPinned))
            .from(normalBoard)
            .where(eqMemberId(memberId)
                    .and(eqNormalBoardType(boardType))
                    .and(likeTitle(search))
                    .or(likeContent(search)))
            .orderBy(normalBoard.dateCreated.desc())
            .fetch();
  }

  @Override
  public List<NormalBoardDto> findAllByTypeAndSearch(NormalBoardType boardType, String search) {
    return queryFactory
            .select(
                    Projections.constructor(
                            NormalBoardDto.class,
                            normalBoard.id,
                            normalBoard.title.value,
                            normalBoard.writer.name.value,
                            normalBoard.dateCreated,
                            normalBoard.dateUpdated,
                            normalBoard.isPinned))
            .from(normalBoard)
            .where(eqNormalBoardType(boardType)
                    .and(likeTitle(search))
                    .or(likeContent(search)))
            .orderBy(normalBoard.dateCreated.desc())
            .fetch();
  }

  @Override
  public Optional<NormalBoard> findByMemberIdAndTypeAndId(Long memberId, NormalBoardType boardType, Long boardId) {
    return Optional.ofNullable(queryFactory
            .selectFrom(normalBoard)
            .where(eqMemberId(memberId)
                    .and(eqNormalBoardType(boardType))
                    .and(normalBoard.id.eq(boardId)))
            .orderBy(normalBoard.dateCreated.desc())
            .fetchOne());
  }

  @Override
  public Optional<NormalBoard> findByTypeAndId(NormalBoardType boardType, Long boardId) {
    return Optional.ofNullable(queryFactory
            .selectFrom(normalBoard)
            .where((eqNormalBoardType(boardType))
                    .and(normalBoard.id.eq(boardId)))
            .orderBy(normalBoard.dateCreated.desc())
            .fetchOne());
  }

  private BooleanExpression eqMemberId(Long memberId) {
    return normalBoard.writer.id.eq(memberId);
  }

  private BooleanExpression eqNormalBoardType(NormalBoardType normalBoardType) {
    return normalBoard.menu.id.eq(normalBoardType.getMenuId());
  }

  private BooleanExpression likeTitle(String search) {
    return normalBoard.title.value.like(search);
  }

  private BooleanExpression likeContent(String search) {
    return normalBoard.content.value.like(search);
  }

}
