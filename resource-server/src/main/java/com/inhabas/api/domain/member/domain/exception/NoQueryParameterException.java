package com.inhabas.api.domain.member.domain.exception;

public class NoQueryParameterException extends IllegalArgumentException {
    private static final String DEFAULT_MESSAGE = "쿼리 파라미터가 아무것도 전달되지 않았습니다.";

    public NoQueryParameterException() {
        super(DEFAULT_MESSAGE);
    }

    public NoQueryParameterException(String message) {
        super(message);
    }
}
