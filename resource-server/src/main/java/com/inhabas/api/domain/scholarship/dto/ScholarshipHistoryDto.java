package com.inhabas.api.domain.scholarship.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.scholarship.domain.ScholarshipHistory;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class ScholarshipHistoryDto {

  @NotNull @Positive private Long id;

  @NotNull private String title;

  @NotNull @Positive private Long writerId;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2023-11-01T00:00:00")
  private LocalDateTime dateHistory;

  public ScholarshipHistoryDto(ScholarshipHistory scholarshipHistory) {
    this.id = scholarshipHistory.getId();
    this.title = scholarshipHistory.getTitle().getValue();
    this.dateHistory = scholarshipHistory.getDateHistory();
  }
}
