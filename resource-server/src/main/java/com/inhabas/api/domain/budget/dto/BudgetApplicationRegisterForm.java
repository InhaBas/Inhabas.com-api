package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.menu.domain.Menu;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetApplicationRegisterForm {

  @NotBlank private String title;

  @NotBlank private String details;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  @Past
  private LocalDateTime dateUsed;

  @NotNull @PositiveOrZero private Integer outcome;

  @NotBlank private String account;

  @NotNull
  @Size(min = 1)
  private List<String> files = new ArrayList<>();

  @Builder
  public BudgetApplicationRegisterForm(
      String title,
      LocalDateTime dateUsed,
      String details,
      Integer outcome,
      String account,
      List<String> files) {
    this.title = title;
    this.dateUsed = dateUsed;
    this.details = details;
    this.outcome = outcome;
    this.account = account;
    this.files = files == null ? new ArrayList<>() : files;
  }

  public BudgetSupportApplication toEntity(Menu menu, Member applicant) {
    return BudgetSupportApplication.builder()
        .menu(menu)
        .title(this.title)
        .details(this.details)
        .dateUsed(this.dateUsed)
        .account(this.account)
        .outcome(this.outcome)
        .applicant(applicant)
        .status(RequestStatus.PENDING)
        .build();
  }
}
