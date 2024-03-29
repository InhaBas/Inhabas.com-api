package com.inhabas.api.domain.myInfo.repository;

import static com.inhabas.api.domain.board.domain.QBaseBoard.baseBoard;
import static com.inhabas.api.domain.budget.domain.QBudgetSupportApplication.budgetSupportApplication;
import static com.inhabas.api.domain.comment.domain.QComment.comment;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.myInfo.dto.MyBoardDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class MyInfoRepositoryImpl implements MyInfoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<MyBoardDto> findAllBoardsByMemberId(Long memberId) {
    return queryFactory
        .select(
            Projections.constructor(
                MyBoardDto.class,
                baseBoard.id,
                baseBoard.menu.id,
                baseBoard.menu.type,
                baseBoard.menu.name.value,
                baseBoard.title.value,
                baseBoard.dateCreated))
        .from(baseBoard)
        .where(
            baseBoard
                .writer
                .id
                .eq(memberId)
                // budgetSupportApplication, budgetHistory은 게시판 조회 범주에서 제외
                .and(baseBoard.instanceOf(BudgetSupportApplication.class).not())
                .and(baseBoard.instanceOf(BudgetHistory.class).not()))
        .orderBy(baseBoard.dateCreated.desc())
        .fetch();
  }

  @Override
  public List<MyCommentDto> findAllCommentsByMemberId(Long memberId) {
    List<Comment> comments =
        queryFactory
            .selectFrom(comment)
            .where(comment.writer.id.eq(memberId))
            .orderBy(comment.dateCreated.desc())
            .fetch();

    return comments.stream()
        .map(
            comment ->
                new MyCommentDto(
                    comment.getParentBoard().getId(),
                    comment.getParentBoard().getMenu().getId(),
                    comment.getParentBoard().getMenu().getType(),
                    comment.getParentBoard().getMenu().getName(),
                    comment.getContent(),
                    comment.getDateCreated()))
        .collect(Collectors.toList());
  }

  @Override
  public List<MyBudgetSupportApplicationDto> findAllBudgetSupportApplicationsByMemberId(
      Long memberId) {
    return queryFactory
        .select(
            Projections.constructor(
                MyBudgetSupportApplicationDto.class,
                budgetSupportApplication.id,
                budgetSupportApplication.status,
                budgetSupportApplication.title.value,
                budgetSupportApplication.dateCreated,
                budgetSupportApplication.dateChecked,
                budgetSupportApplication.dateDeposited))
        .from(budgetSupportApplication)
        .where(budgetSupportApplication.applicant.id.eq(memberId))
        .orderBy(budgetSupportApplication.dateCreated.desc())
        .fetch();
  }
}
