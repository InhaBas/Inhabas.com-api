package com.inhabas.api.domain.project.repository;

import static com.inhabas.api.domain.project.domain.QProjectBoard.projectBoard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.project.domain.ProjectBoard;
import com.inhabas.api.domain.project.domain.ProjectBoardType;
import com.inhabas.api.domain.project.dto.ProjectBoardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ProjectBoardRepositoryImpl implements ProjectBoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<ProjectBoardDto> findAllByTypeAndIsPinned(ProjectBoardType projectBoardType) {
    return queryFactory
        .select(
            Projections.constructor(
                ProjectBoardDto.class,
                projectBoard.id,
                projectBoard.title.value,
                projectBoard.writer.id,
                projectBoard.writer.name.value,
                projectBoard.datePinExpiration,
                projectBoard.dateCreated,
                projectBoard.dateUpdated,
                projectBoard.isPinned))
        .from(projectBoard)
        .where(
            eqProjectBoardType(projectBoardType)
                .and(projectBoard.isPinned.isTrue())
                .and(projectBoard.datePinExpiration.after(LocalDateTime.now())))
        .orderBy(projectBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<ProjectBoardDto> findAllByMemberIdAndTypeAndSearch(
      Long memberId, ProjectBoardType projectBoardType, String search) {
    return queryFactory
        .select(
            Projections.constructor(
                ProjectBoardDto.class,
                projectBoard.id,
                projectBoard.title.value,
                projectBoard.writer.id,
                projectBoard.writer.name.value,
                projectBoard.datePinExpiration,
                projectBoard.dateCreated,
                projectBoard.dateUpdated,
                projectBoard.isPinned))
        .from(projectBoard)
        .where(
            eqMemberId(memberId)
                .and(eqProjectBoardType(projectBoardType))
                .and(likeTitle(search).or(likeContent(search))))
        .orderBy(projectBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<ProjectBoardDto> findAllByTypeAndSearch(
      ProjectBoardType projectBoardType, String search) {
    return queryFactory
        .select(
            Projections.constructor(
                ProjectBoardDto.class,
                projectBoard.id,
                projectBoard.title.value,
                projectBoard.writer.id,
                projectBoard.writer.name.value,
                projectBoard.datePinExpiration,
                projectBoard.dateCreated,
                projectBoard.dateUpdated,
                projectBoard.isPinned))
        .from(projectBoard)
        .where(eqProjectBoardType(projectBoardType).and(likeTitle(search).or(likeContent(search))))
        .orderBy(projectBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public Optional<ProjectBoard> findByMemberIdAndTypeAndId(
      Long memberId, ProjectBoardType projectBoardType, Long boardId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(projectBoard)
            .where(
                eqMemberId(memberId)
                    .and(eqProjectBoardType(projectBoardType))
                    .and(projectBoard.id.eq(boardId)))
            .orderBy(projectBoard.dateCreated.desc())
            .fetchOne());
  }

  @Override
  public Optional<ProjectBoard> findByTypeAndId(ProjectBoardType projectBoardType, Long boardId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(projectBoard)
            .where((eqProjectBoardType(projectBoardType)).and(projectBoard.id.eq(boardId)))
            .orderBy(projectBoard.dateCreated.desc())
            .fetchOne());
  }

  private BooleanExpression eqMemberId(Long memberId) {
    return projectBoard.writer.id.eq(memberId);
  }

  private BooleanExpression eqProjectBoardType(ProjectBoardType projectBoardType) {
    return projectBoard.menu.id.eq(projectBoardType.getMenuId());
  }

  private BooleanExpression likeTitle(String search) {
    return projectBoard.title.value.like("%" + search + "%");
  }

  private BooleanExpression likeContent(String search) {
    return projectBoard.content.value.like("%" + search + "%");
  }
}
