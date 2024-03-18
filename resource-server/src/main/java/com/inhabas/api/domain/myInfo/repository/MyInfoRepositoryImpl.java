package com.inhabas.api.domain.myInfo.repository;

import static com.inhabas.api.domain.contest.domain.QContestBoard.contestBoard;
import static com.inhabas.api.domain.normalBoard.domain.QNormalBoard.normalBoard;
import static com.inhabas.api.domain.project.domain.QProjectBoard.projectBoard;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.myInfo.dto.MyPostsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class MyInfoRepositoryImpl implements MyInfoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<MyPostsDto> findAllNormalBoardsByMemberId(Long memberId) {
    return queryFactory
        .select(
            Projections.constructor(
                MyPostsDto.class,
                normalBoard.id,
                normalBoard.menu.name,
                normalBoard.title,
                normalBoard.dateCreated))
        .from(normalBoard)
        .where(normalBoard.writer.id.eq(memberId))
        .orderBy(normalBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<MyPostsDto> findAllProjectBoardsByMemberId(Long memberId) {
    return queryFactory
        .select(
            Projections.constructor(
                MyPostsDto.class,
                projectBoard.id,
                projectBoard.menu.name,
                projectBoard.title,
                projectBoard.dateCreated))
        .from(projectBoard)
        .where(projectBoard.writer.id.eq(memberId))
        .orderBy(projectBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<MyPostsDto> findAllContestBoardsByMemberId(Long memberId) {
    return queryFactory
        .select(
            Projections.constructor(
                MyPostsDto.class,
                contestBoard.id,
                contestBoard.menu.name,
                contestBoard.title,
                contestBoard.dateCreated))
        .from(contestBoard)
        .where(contestBoard.writer.id.eq(memberId))
        .orderBy(contestBoard.dateCreated.desc())
        .fetch();
  }
}
