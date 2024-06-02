package com.inhabas.api.domain.contest.domain.valueObject;

import java.time.LocalDateTime;

import com.inhabas.api.domain.contest.domain.QContestBoard;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;

// 공모전 게시판 정렬 및 필터링 기준입니다.
public enum OrderBy {

  // 공모전 게시판을 마감일이 늦은순으로 정렬하는 기준입니다. 마감일이 지난 게시글도 포함됩니다.
  ALL {
    public OrderSpecifier<?> getOrderBy(QContestBoard contestBoard) {
      return contestBoard.dateContestEnd.desc();
    }

    public BooleanExpression getFilter(QContestBoard contestBoard) {
      return null;
    }
  },

  // 공모전 게시판을 마감일이 임박한 순으로 정렬하는 기준입니다. 마감일이 지난 게시글은 제외합니다.
  DUE_DATE {

    // 마감순 정렬 로직 설명:
    // 1. 게시물을 마감일 기준 오름차순으로 정렬합니다.
    // 2. 마감일이 지나지 않은 게시물만 필터링합니다.
    // 3. 결과적으로 마감일이 빠른 순으로 정렬됩니다.

    // 게시물을 마감일기준 오름차순으로 정렬합니다.
    public OrderSpecifier<?> getOrderBy(QContestBoard contestBoard) {
      return contestBoard.dateContestEnd.asc();
    }

    // 마감일이 지나지 않은 게시글만 필터링합니다.
    public BooleanExpression getFilter(QContestBoard contestBoard) {
      return contestBoard.dateContestEnd.goe(LocalDateTime.now());
    }
  };

  public abstract OrderSpecifier<?> getOrderBy(QContestBoard contestBoard);

  public abstract BooleanExpression getFilter(QContestBoard contestBoard);
}
