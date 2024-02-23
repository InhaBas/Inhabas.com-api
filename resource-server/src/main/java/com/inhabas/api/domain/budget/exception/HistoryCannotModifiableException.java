package com.inhabas.api.domain.budget.exception;

import org.springframework.security.access.AccessDeniedException;

public class HistoryCannotModifiableException extends AccessDeniedException {

  public HistoryCannotModifiableException() {
    super("다른 담당자가 작성한 내역은 수정할 수 없습니다!");
  }

  public HistoryCannotModifiableException(String msg) {
    super(msg);
  }
}
