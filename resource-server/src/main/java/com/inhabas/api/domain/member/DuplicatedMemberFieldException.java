package com.inhabas.api.domain.member;

public class DuplicatedMemberFieldException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "중복된 필드 값이 입력되었습니다.";

    public DuplicatedMemberFieldException() {
        super(DEFAULT_MESSAGE);
    }

    public DuplicatedMemberFieldException(String field) {
        super(String.format("%s 이(가) 중복됩니다.", field));
    }
}
