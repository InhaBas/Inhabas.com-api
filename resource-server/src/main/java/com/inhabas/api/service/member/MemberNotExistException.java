package com.inhabas.api.service.member;

public class MemberNotExistException extends RuntimeException {

    private static final String defaultMessage = "해당하는 멤버가 존재하지 않습니다.";

    public MemberNotExistException() {
        super(defaultMessage);
    }

    public MemberNotExistException(String message) {
        super(message);
    }
}
