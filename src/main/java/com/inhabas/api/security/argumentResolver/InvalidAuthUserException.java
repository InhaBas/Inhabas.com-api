package com.inhabas.api.security.argumentResolver;

public class InvalidAuthUserException extends Exception {
    private static final String defaultMessage  = "회원가입 후 이용해주세요.";

    public InvalidAuthUserException() {
        super(defaultMessage);
    }

    public InvalidAuthUserException(String message) {
        super(message);
    }
}
