package com.inhabas.api.domain.project.repository;

import static com.inhabas.api.domain.normalBoard.domain.QNormalBoard.normalBoard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.project.ProjectBoardType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ProjectBoardRepositoryImpl implements ProjectBoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<NormalBoardDto> findAllByTypeAndIsPinned(ProjectBoardType projectBoardType) {
    return queryFactory
        .select(
            Projections.constructor(
                NormalBoardDto.class,
                normalBoard.id,
                normalBoard.title.value,
                normalBoard.writer.id,
                normalBoard.writer.name.value,
                normalBoard.datePinExpiration,
                normalBoard.dateCreated,
                normalBoard.dateUpdated,
                normalBoard.isPinned))
        .from(normalBoard)
        .where(
            eqProjectBoardType(projectBoardType)
                .and(normalBoard.isPinned.isTrue())
                .and(normalBoard.datePinExpiration.after(LocalDateTime.now())))
        .orderBy(normalBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<NormalBoardDto> findAllByMemberIdAndTypeAndSearch(
      Long memberId, ProjectBoardType projectBoardType, String search) {
    return queryFactory
        .select(
            Projections.constructor(
                NormalBoardDto.class,
                normalBoard.id,
                normalBoard.title.value,
                normalBoard.writer.id,
                normalBoard.writer.name.value,
                normalBoard.datePinExpiration,
                normalBoard.dateCreated,
                normalBoard.dateUpdated,
                normalBoard.isPinned))
        .from(normalBoard)
        .where(
            eqMemberId(memberId)
                .and(eqProjectBoardType(projectBoardType))
                .and(likeTitle(search).or(likeContent(search))))
        .orderBy(normalBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<NormalBoardDto> findAllByTypeAndSearch(
      ProjectBoardType projectBoardType, String search) {
    return queryFactory
        .select(
            Projections.constructor(
                NormalBoardDto.class,
                normalBoard.id,
                normalBoard.title.value,
                normalBoard.writer.id,
                normalBoard.writer.name.value,
                normalBoard.datePinExpiration,
                normalBoard.dateCreated,
                normalBoard.dateUpdated,
                normalBoard.isPinned))
        .from(normalBoard)
        .where(eqProjectBoardType(projectBoardType).and(likeTitle(search).or(likeContent(search))))
        .orderBy(normalBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public Optional<NormalBoard> findByMemberIdAndTypeAndId(
      Long memberId, ProjectBoardType projectBoardType, Long boardId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(normalBoard)
            .where(
                eqMemberId(memberId)
                    .and(eqProjectBoardType(projectBoardType))
                    .and(normalBoard.id.eq(boardId)))
            .orderBy(normalBoard.dateCreated.desc())
            .fetchOne());
  }

  @Override
  public Optional<NormalBoard> findByTypeAndId(ProjectBoardType projectBoardType, Long boardId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(normalBoard)
            .where((eqProjectBoardType(projectBoardType)).and(normalBoard.id.eq(boardId)))
            .orderBy(normalBoard.dateCreated.desc())
            .fetchOne());
  }

  private BooleanExpression eqMemberId(Long memberId) {
    return normalBoard.writer.id.eq(memberId);
  }

  private BooleanExpression eqProjectBoardType(ProjectBoardType projectBoardType) {
    return normalBoard.menu.id.eq(projectBoardType.getMenuId());
  }

  private BooleanExpression likeTitle(String search) {
    return normalBoard.title.value.like("%" + search + "%");
  }

  private BooleanExpression likeContent(String search) {
    return normalBoard.content.value.like("%" + search + "%");
  }
}
