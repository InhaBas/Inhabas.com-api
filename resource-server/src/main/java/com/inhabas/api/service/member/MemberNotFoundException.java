package com.inhabas.api.service.member;

public class MemberNotFoundException extends RuntimeException {

    private static final String defaultMessage = "해당하는 멤버가 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(defaultMessage);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
