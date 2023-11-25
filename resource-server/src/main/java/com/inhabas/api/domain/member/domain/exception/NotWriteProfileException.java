package com.inhabas.api.domain.member.domain.exception;

import org.springframework.security.access.AccessDeniedException;

public class NotWriteProfileException extends AccessDeniedException {
    private static final String DEFAULT_MESSAGE = "아직 회원 프로필을 생성하지 않아서 회원가입을 마무리 할 수 없습니다.";

    public NotWriteProfileException() {
        super(DEFAULT_MESSAGE);
    }

    public NotWriteProfileException(String msg) {
        super(msg);
    }
}
