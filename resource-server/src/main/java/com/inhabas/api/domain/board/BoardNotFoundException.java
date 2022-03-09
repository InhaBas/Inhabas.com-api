package com.inhabas.api.domain.board;

import javax.persistence.EntityNotFoundException;

public class BoardNotFoundException extends EntityNotFoundException {
    private static final String defaultMessage  = "게시글을 찾을 수 없습니다.";

    public BoardNotFoundException() {
        super(defaultMessage);
    }

    public BoardNotFoundException(String message) {
        super(message);
    }

}
