package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetApplicationRegisterForm {

  @NotBlank private String title;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Past
  @NotNull
  private LocalDateTime dateUsed;

  @NotBlank private String details;

  @NotNull @Positive private Integer outcome;

  @NotBlank private String accounts;

  public BudgetApplicationRegisterForm(
      String title, LocalDateTime dateUsed, String details, Integer outcome, String accounts) {
    this.title = title;
    this.dateUsed = dateUsed;
    this.details = details;
    this.outcome = outcome;
    this.accounts = accounts;
  }

  public BudgetSupportApplication toEntity(Member applicationWriter) {
    return BudgetSupportApplication.builder()
        .title(this.title)
        .dateUsed(this.dateUsed)
        .details(this.details)
        .outcome(this.outcome)
        .account(this.accounts)
        .applicationWriter(applicationWriter)
        .build();
  }
}
