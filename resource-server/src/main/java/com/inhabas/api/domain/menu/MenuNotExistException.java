package com.inhabas.api.domain.menu;

public class MenuNotExistException extends RuntimeException {

    private static final String defaultMessage = "해당하는 메뉴가 존재하지 않습니다.";

    public MenuNotExistException() {
        super(defaultMessage);
    }

    public MenuNotExistException(String message) {
        super(message);
    }
}
