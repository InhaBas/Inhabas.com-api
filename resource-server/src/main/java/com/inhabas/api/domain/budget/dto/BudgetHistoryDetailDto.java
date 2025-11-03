package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class BudgetHistoryDetailDto {

  @NotNull private Long id;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  @Past
  private LocalDateTime dateUsed;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  @NotBlank private String title;

  @NotBlank private String details;

  @NotNull @PositiveOrZero private Integer income;

  @NotNull @PositiveOrZero private Integer outcome;

  @NotBlank private String account;

  @NotNull private Long memberIdReceived;

  @NotNull private String memberStudentIdReceived;

  @NotBlank private String memberNameReceived;

  @NotNull private String memberStudentIdInCharge;

  @NotBlank private String memberNameInCharge;

  @NotNull private List<FileDownloadDto> receipts;

  @Builder
  public BudgetHistoryDetailDto(
      Long id,
      LocalDateTime dateUsed,
      LocalDateTime dateCreated,
      LocalDateTime dateUpdated,
      String title,
      String details,
      Integer income,
      Integer outcome,
      String account,
      Long memberIdReceived,
      String memberStudentIdReceived,
      String memberNameReceived,
      String memberStudentIdInCharge,
      String memberNameInCharge,
      List<FileDownloadDto> receipts) {
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
    this.memberStudentIdReceived = memberStudentIdReceived;
    this.memberNameReceived = memberNameReceived;
    this.memberStudentIdInCharge = memberStudentIdInCharge;
    this.memberNameInCharge = memberNameInCharge;
    this.receipts = receipts;
  }
}
