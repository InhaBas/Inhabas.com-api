package com.inhabas.api.domain.budget;

public class NotFoundBudgetHistoryException extends RuntimeException {

    public NotFoundBudgetHistoryException() {
        super("cannot find such budget history!");
    }

    public NotFoundBudgetHistoryException(String message) {
        super(message);
    }
}
