package com.inhabas.api.domain.scholarship.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class SaveScholarshipHistoryDto {

  @NotBlank String title;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  @Past
  LocalDateTime dateHistory;

  @Builder
  public SaveScholarshipHistoryDto(String title, LocalDateTime dateHistory) {
    this.title = title;
    this.dateHistory = dateHistory;
  }
}
