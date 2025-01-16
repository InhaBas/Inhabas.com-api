package com.inhabas.api.domain.scholarship.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class SaveScholarshipBoardDto {

  @NotBlank private String title;

  @NotBlank private String content;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  @Past
  private LocalDateTime dateHistory;

  private List<String> files = new ArrayList<>();

  @Builder
  public SaveScholarshipBoardDto(
      String title, String content, LocalDateTime dateHistory, List<String> files) {
    this.title = title;
    this.content = content;
    this.dateHistory = dateHistory;
    this.files = files == null ? new ArrayList<>() : files;
  }
}
