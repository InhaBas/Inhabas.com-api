package com.inhabas.api.domain.budget.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BudgetHistoryDetailDto {

  @NotNull
  private Integer id;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  @Past
  private LocalDateTime dateUsed;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  @NotBlank
  private String title;

  @NotBlank
  private String details;

  @NotNull
  @PositiveOrZero
  private Integer income;

  @NotNull
  @PositiveOrZero
  private Integer outcome;

  @NotBlank
  private String account;

  @NotNull
  private Long memberIdReceived;

  @NotBlank
  private String memberNameReceived;

  @NotNull
  private Long memberIdInCharge;

  @NotBlank
  private String memberNameInCharge;

  @Builder
  public BudgetHistoryDetailDto(Integer id, LocalDateTime dateUsed, LocalDateTime dateCreated,
      LocalDateTime dateUpdated, String title, String details, Integer income, Integer outcome,
      String account, Long memberIdReceived, String memberNameReceived, Long memberIdInCharge,
      String memberNameInCharge) {
    this.id = id;
    this.dateUsed = dateUsed;
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
    this.title = title;
    this.details = details;
    this.income = income;
    this.outcome = outcome;
    this.account = account;
    this.memberIdReceived = memberIdReceived;
    this.memberNameReceived = memberNameReceived;
    this.memberIdInCharge = memberIdInCharge;
    this.memberNameInCharge = memberNameInCharge;
  }
}
