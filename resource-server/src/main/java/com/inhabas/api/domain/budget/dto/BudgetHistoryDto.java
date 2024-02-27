package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class BudgetHistoryDto {

  @NotNull private Long id;

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

  @NotBlank private String title;

  @NotNull @PositiveOrZero private Integer income;

  @NotNull @PositiveOrZero private Integer outcome;

  @NotNull private String memberStudentIdReceived;

  @NotBlank private String memberNameReceived;

  @NotNull private String memberStudentIdInCharge;

  @NotBlank private String memberNameInCharge;

  @Builder
  public BudgetHistoryDto(
      Long id,
      LocalDateTime dateUsed,
      LocalDateTime dateCreated,
      LocalDateTime dateUpdated,
      String title,
      Integer income,
      Integer outcome,
      String memberStudentIdReceived,
      String memberNameReceived,
      String memberStudentIdInCharge,
      String memberNameInCharge) {
    this.id = id;
    this.dateUsed = dateUsed;
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
    this.title = title;
    this.income = income;
    this.outcome = outcome;
    this.memberStudentIdReceived = memberStudentIdReceived;
    this.memberNameReceived = memberNameReceived;
    this.memberStudentIdInCharge = memberStudentIdInCharge;
    this.memberNameInCharge = memberNameInCharge;
  }
}
