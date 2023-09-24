package com.inhabas.api.domain.member.domain.exception;

public class MemberNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "해당하는 멤버가 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
