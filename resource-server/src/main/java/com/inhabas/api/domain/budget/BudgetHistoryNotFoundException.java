package com.inhabas.api.domain.budget;

public class BudgetHistoryNotFoundException extends RuntimeException {

  public BudgetHistoryNotFoundException() {
    super("cannot find such budget history!");
  }

  public BudgetHistoryNotFoundException(String message) {
    super(message);
  }
}
