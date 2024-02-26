package com.inhabas.api.domain.contest.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.QContestBoard;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.contest.domain.valueObject.OrderBy;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ContestBoardRepositoryImpl implements ContestBoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private QContestBoard contestBoard = QContestBoard.contestBoard;

  // 공모전 검색 및 필터링 기능
  public List<ContestBoardDto> findAllByTypeAndFieldAndSearch(
      ContestType contestType, Long contestFieldId, String search, OrderBy orderBy) {

    BooleanExpression target =
        eqContestType(contestType)
            .and(eqContestField(contestFieldId))
            .and(
                likeTitle(search)
                    .or(likeContent(search))
                    .or(likeWriterName(search))
                    .or(likeAssociation(search))
                    .or(likeTopic(search)));

    OrderSpecifier<?> order = orderBy.getOrderBy(contestBoard);

    List<ContestBoard> boards =
        queryFactory.selectFrom(contestBoard).where(target).orderBy(order).fetch();

    return boards.stream()
        .map(
            board -> {
              ClassifiedFiles classifiedFiles =
                  ClassifyFiles.classifyFiles(new ArrayList<>(board.getFiles()));
              return ContestBoardDto.builder()
                  .id(board.getId())
                  .contestFieldId(board.getContestField().getId())
                  .title(board.getTitle())
                  .topic(board.getTopic())
                  .association(board.getAssociation())
                  .dateContestStart(board.getDateContestStart())
                  .dateContestEnd(board.getDateContestEnd())
                  .thumbnail(classifiedFiles.getThumbnail())
                  .build();
            })
        .collect(Collectors.toList());
  }

  @Override
  public Optional<ContestBoard> findByTypeAndId(ContestType contestType, Long boardId) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(contestBoard)
            .where((eqContestType(contestType)).and(contestBoard.id.eq(boardId)))
            .orderBy(contestBoard.dateCreated.desc())
            .fetchOne());
  }

  private BooleanExpression eqMemberId(Long memberId) {
    return contestBoard.writer.id.eq(memberId);
  }

  private BooleanExpression eqContestType(ContestType contestType) {
    return contestBoard.menu.id.eq(contestType.getMenuId());
  }

  private BooleanExpression eqContestField(Long contestFieldId) {
    if (contestFieldId == null) {
      return Expressions.asBoolean(true).isTrue();
    }
    return contestBoard.contestField.id.eq(contestFieldId);
  }

  private BooleanExpression likeTitle(String search) {
    return contestBoard.title.value.like("%" + search + "%");
  }

  private BooleanExpression likeContent(String search) {
    return contestBoard.content.value.like("%" + search + "%");
  }

  private BooleanExpression likeWriterName(String search) {
    return contestBoard.writer.name.value.like("%" + search + "%");
  }

  private BooleanExpression likeAssociation(String search) {
    return contestBoard.association.value.like("%" + search + "%");
  }

  private BooleanExpression likeTopic(String search) {
    return contestBoard.association.value.like("%" + search + "%");
  }
}
