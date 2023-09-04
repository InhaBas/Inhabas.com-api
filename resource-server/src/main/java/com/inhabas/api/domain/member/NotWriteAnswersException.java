package com.inhabas.api.domain.member;

import org.springframework.security.access.AccessDeniedException;

public class NotWriteAnswersException extends AccessDeniedException {
    private static final String DEFAULT_MESSAGE = "아직 면접 질문을 작성하지 않아서 회원가입을 마무리 할 수 없습니다.";

    public NotWriteAnswersException() {
        super(DEFAULT_MESSAGE);
    }

    public NotWriteAnswersException(String msg) {
        super(msg);
    }
}
