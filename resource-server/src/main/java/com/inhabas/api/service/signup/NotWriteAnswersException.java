package com.inhabas.api.service.signup;

import org.springframework.security.access.AccessDeniedException;

public class NotWriteAnswersException extends AccessDeniedException {
    private static final String defaultMessage = "아직 면접 질문을 작성하지 않아서 회원가입을 마무리 할 수 없습니다.";

    public NotWriteAnswersException() {
        super(defaultMessage);
    }

    public NotWriteAnswersException(String msg) {
        super(msg);
    }
}
