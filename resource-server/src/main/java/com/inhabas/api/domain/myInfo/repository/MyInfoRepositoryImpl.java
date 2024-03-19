package com.inhabas.api.domain.myInfo.repository;

import static com.inhabas.api.domain.board.domain.QBaseBoard.baseBoard;
import static com.inhabas.api.domain.budget.domain.QBudgetSupportApplication.budgetSupportApplication;
import static com.inhabas.api.domain.comment.domain.QComment.comment;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.myInfo.dto.MyBoardsDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class MyInfoRepositoryImpl implements MyInfoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<MyBoardsDto> findAllBoardsByMemberId(Long memberId) {
    return queryFactory
        .select(
            Projections.constructor(
                MyBoardsDto.class,
                baseBoard.id,
                baseBoard.menu.id,
                baseBoard.menu.name.value,
                baseBoard.title.value,
                baseBoard.dateCreated))
        .from(baseBoard)
        .where(baseBoard.writer.id.eq(memberId))
        .orderBy(baseBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<MyCommentsDto> findAllCommentsByMemberId(Long memberId) {
    return queryFactory
        .select(
            Projections.constructor(
                MyCommentsDto.class,
                comment.parentBoard.id,
                comment.parentBoard.menu.id,
                comment.parentBoard.menu.name.value,
                comment.content.value,
                comment.dateCreated))
        .from(comment)
        .where(comment.writer.id.eq(memberId))
        .orderBy(comment.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<MyBudgetSupportApplicationDto> findAllBudgetSupportAllpicationByMemberId(
      Long memberId) {
    return queryFactory
        .select(
            Projections.constructor(
                MyBudgetSupportApplicationDto.class,
                budgetSupportApplication.id,
                budgetSupportApplication.status,
                budgetSupportApplication.title.value,
                budgetSupportApplication.dateCreated,
                budgetSupportApplication.dateChecked))
        .from(budgetSupportApplication)
        .where(budgetSupportApplication.applicant.id.eq(memberId))
        .orderBy(budgetSupportApplication.dateCreated.desc())
        .fetch();
  }
}
