package com.inhabas.api.domain.contest.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.QContestBoard;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class ContestBoardRepositoryImpl implements ContestBoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private QContestBoard contestBoard = QContestBoard.contestBoard;

  // 필드 이름과 정렬 기준을 매핑
  private OrderSpecifier<?> getSortedColumn(
      Order order, QContestBoard contestBoard, String fieldName) {
    Map<String, BiFunction<Order, QContestBoard, OrderSpecifier<?>>> orderSpecifierMap =
        new HashMap<>();
    orderSpecifierMap.put("id", (o, cb) -> new OrderSpecifier<>(o, cb.id));
    orderSpecifierMap.put("dateContestEnd", (o, cb) -> new OrderSpecifier<>(o, cb.dateContestEnd));

    return orderSpecifierMap
        .getOrDefault(fieldName, (o, cb) -> new OrderSpecifier<>(Order.DESC, cb.dateContestEnd))
        .apply(order, contestBoard);
  }

  // 공모전 검색 및 필터링 기능
  public List<ContestBoardDto> findAllByTypeAndFieldAndSearch(
      ContestType contestType, Long contestFieldId, String search, String sortBy) {
    BooleanExpression target =
        eqContestType(contestType)
            .and(eqContestField(contestFieldId))
            .and(
                titleLike(search)
                    .or(contentLike(search))
                    .or(writerNameLike(search))
                    .or(associationLike(search))
                    .or(topicLike(search)));
    JPAQuery<ContestBoard> query = queryFactory.selectFrom(contestBoard).where(target);

    if ("boardId".equals(sortBy)) {
      query.orderBy(contestBoard.id.desc()); // 최신순: boardId 순으로 내림차순 정렬
    } else if ("dateContestEnd".equals(sortBy)) {
      query.orderBy(contestBoard.dateContestEnd.desc()); // 마감순: dateContestEnd 순으로 내림차순 정렬
    }

    List<ContestBoard> boards = query.fetch();

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
    return contestBoard.contestType.eq(contestType);
  }

  private BooleanExpression eqContestField(Long contestFieldId) {
    if (contestFieldId == null) {
      return null;
    }
    return contestBoard.contestField.id.eq(contestFieldId);
  }

  private BooleanExpression titleLike(String search) {
    return hasText(search) ? contestBoard.title.value.containsIgnoreCase(search) : null;
  }

  private BooleanExpression contentLike(String search) {
    return hasText(search) ? contestBoard.content.value.containsIgnoreCase(search) : null;
  }

  private BooleanExpression writerNameLike(String search) {
    return hasText(search) ? contestBoard.writer.name.value.containsIgnoreCase(search) : null;
  }

  private BooleanExpression associationLike(String search) {
    return hasText(search) ? contestBoard.association.value.containsIgnoreCase(search) : null;
  }

  private BooleanExpression topicLike(String search) {
    return hasText(search) ? contestBoard.topic.value.containsIgnoreCase(search) : null;
  }

  private Boolean hasText(String text) {
    return text != null && !text.trim().isEmpty();
  }
}
