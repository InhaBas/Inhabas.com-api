package com.inhabas.api.domain.board.exception;

import org.springframework.security.access.AccessDeniedException;

public class OnlyWriterModifiableException extends AccessDeniedException {

  public OnlyWriterModifiableException() {
    super("다른 작성자가 작성한 글은 수정할 수 없습니다!");
  }

  public OnlyWriterModifiableException(String msg) {
    super(msg);
  }
}
