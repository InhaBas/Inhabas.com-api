package com.inhabas.api.domain.contest.domain.valueObject;

import com.inhabas.api.domain.contest.domain.QContestBoard;
import com.querydsl.core.types.OrderSpecifier;

public enum OrderBy {
  DATE_CONTEST_END {
    public OrderSpecifier<?> getOrderBy(QContestBoard contestBoard) {
      return contestBoard.dateContestEnd.desc();
    }
  },

  DATE_CREATED {
    public OrderSpecifier<?> getOrderBy(QContestBoard contestBoard) {
      return contestBoard.dateCreated.desc();
    }
  };

  public abstract OrderSpecifier<?> getOrderBy(QContestBoard contestBoard);
}
