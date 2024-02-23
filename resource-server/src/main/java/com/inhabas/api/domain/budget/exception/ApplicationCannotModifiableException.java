package com.inhabas.api.domain.budget.exception;

import org.springframework.security.access.AccessDeniedException;

public class ApplicationCannotModifiableException extends AccessDeniedException {

  public ApplicationCannotModifiableException() {
    super("다른 사람이 작성한 내역은 수정할 수 없습니다!");
  }

  public ApplicationCannotModifiableException(String msg) {
    super(msg);
  }
}
