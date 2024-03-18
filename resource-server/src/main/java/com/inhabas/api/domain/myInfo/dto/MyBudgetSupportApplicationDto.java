package com.inhabas.api.domain.myInfo.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class MyBudgetSupportApplicationDto {

  @NotNull private RequestStatus status;

  @NotBlank private String title;

  @NotNull private List<FileDownloadDto> receipts;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateCreated;
}
