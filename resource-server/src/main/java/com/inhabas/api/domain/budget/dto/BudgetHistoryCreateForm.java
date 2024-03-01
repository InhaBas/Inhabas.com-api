package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.budget.domain.BudgetHistory;
import com.inhabas.api.domain.menu.domain.Menu;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetHistoryCreateForm {

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  @Past
  private LocalDateTime dateUsed;

  @NotBlank private String title;

  private String details;

  @NotNull private String memberStudentIdReceived;

  @NotBlank private String memberNameReceived;

  @PositiveOrZero @NotNull private Integer income;

  @PositiveOrZero @NotNull private Integer outcome;

  @Builder
  public BudgetHistoryCreateForm(
      LocalDateTime dateUsed,
      String title,
      String details,
      String memberStudentIdReceived,
      String memberNameReceived,
      Integer income,
      Integer outcome) {
    this.dateUsed = dateUsed;
    this.title = title;
    this.details = details;
    this.memberStudentIdReceived = memberStudentIdReceived;
    this.memberNameReceived = memberNameReceived;
    this.income = income;
    this.outcome = outcome;

    if (this.details.isBlank()) {
      this.details = this.title;
    }
  }

  public BudgetHistory toEntity(Menu menu, Member secretary, Member memberReceived) {
    return BudgetHistory.builder()
        .title(this.title)
        .menu(menu)
        .details(this.details)
        .dateUsed(this.dateUsed)
        .memberInCharge(secretary)
        .account(null)
        .income(this.income)
        .outcome(this.outcome)
        .memberReceived(memberReceived)
        .build();
  }
}
