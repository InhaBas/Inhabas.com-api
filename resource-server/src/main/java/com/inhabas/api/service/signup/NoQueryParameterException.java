package com.inhabas.api.service.signup;

public class NoQueryParameterException extends IllegalArgumentException {
    private static final String defaultMessage = "쿼리 파라미터가 아무것도 전달되지 않았습니다.";

    public NoQueryParameterException() {
        super(defaultMessage);
    }

    public NoQueryParameterException(String message) {
        super(message);
    }
}
