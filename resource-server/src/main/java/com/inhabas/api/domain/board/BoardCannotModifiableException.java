package com.inhabas.api.domain.board;

import org.springframework.security.access.AccessDeniedException;

public class BoardCannotModifiableException extends AccessDeniedException {

    public BoardCannotModifiableException() {
        super("다른 작성자가 작성한 글은 수정할 수 없습니다!");
    }

    public BoardCannotModifiableException(String msg) {
        super(msg);
    }
}
