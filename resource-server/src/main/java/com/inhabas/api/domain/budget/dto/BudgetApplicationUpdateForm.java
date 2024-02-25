package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetApplicationUpdateForm extends BudgetApplicationRegisterForm {

  public BudgetApplicationUpdateForm(
      String title, LocalDateTime dateUsed, String details, Integer outcome, String accounts) {
    super(title, dateUsed, details, outcome, accounts);
  }
}
