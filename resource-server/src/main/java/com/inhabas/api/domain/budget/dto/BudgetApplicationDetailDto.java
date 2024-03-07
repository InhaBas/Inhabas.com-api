package com.inhabas.api.domain.budget.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.domain.valueObject.RejectReason;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class BudgetApplicationDetailDto {

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

  @NotBlank private String details;

  @NotNull @PositiveOrZero private Integer outcome;

  @NotBlank private String account;

  @NotNull private Long applicantId;

  @NotBlank private String applicantStudentId;

  @NotBlank private String applicantName;

  private Long memberIdInCharge;

  private String memberStudentIdInCharge;

  private String memberNameInCharge;

  @NotNull private RequestStatus status;

  private String rejectReason;

  @NotNull private List<FileDownloadDto> receipts;

  @Builder
  public BudgetApplicationDetailDto(
      Long id,
      LocalDateTime dateUsed,
      LocalDateTime dateCreated,
      LocalDateTime dateUpdated,
      String title,
      String details,
      Integer outcome,
      String account,
      Member applicant,
      Member memberInCharge,
      RequestStatus status,
      RejectReason rejectReason,
      List<FileDownloadDto> receipts) {
    this.id = id;
    this.dateUsed = dateUsed;
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
    this.title = title;
    this.details = details;
    this.outcome = outcome;
    this.account = account;
    this.applicantId = applicant.getId();
    this.applicantStudentId = applicant.getStudentId();
    this.applicantName = applicant.getName();
    this.memberIdInCharge = memberInCharge == null ? null : memberInCharge.getId();
    this.memberStudentIdInCharge = memberInCharge == null ? null : memberInCharge.getStudentId();
    this.memberNameInCharge = memberInCharge == null ? null : memberInCharge.getName();
    this.status = status;
    this.rejectReason = rejectReason == null ? null : rejectReason.getValue();
    this.receipts = receipts;
  }
}
