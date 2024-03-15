package com.inhabas.api.domain.scholarship.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveScholarshipHistoryDto {

  @NotBlank String title;

  @NotNull @Past LocalDateTime dateHistory;

  @Builder
  public SaveScholarshipHistoryDto(String title, LocalDateTime dateHistory) {
    this.title = title;
    this.dateHistory = dateHistory;
  }
}
