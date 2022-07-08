package com.inhabas.api.domain.member;

import org.springframework.security.access.AccessDeniedException;

public class NotWriteProfileException extends AccessDeniedException {
    private static final String defaultMessage = "아직 회원 프로필을 생성하지 않아서 회원가입을 마무리 할 수 없습니다.";

    public NotWriteProfileException() {
        super(defaultMessage);
    }

    public NotWriteProfileException(String msg) {
        super(msg);
    }
}
