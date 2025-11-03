package com.inhabas.api.domain.scholarship.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class ScholarshipBoardDetailDto {

  @NotNull @Positive private Long id;

  @NotBlank private String title;

  @NotBlank private String content;

  @NotNull private Long writerId;

  @NotBlank private String writerName;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateHistory;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  @NotNull private List<FileDownloadDto> images;

  @NotNull private List<FileDownloadDto> otherFiles;

  @Builder
  public ScholarshipBoardDetailDto(
      Long id,
      String title,
      String content,
      Member writer,
      LocalDateTime dateHistory,
      LocalDateTime dateCreated,
      LocalDateTime dateUpdated,
      List<FileDownloadDto> images,
      List<FileDownloadDto> otherFiles) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.writerId = writer.getId();
    this.writerName = writer.getName();
    this.dateHistory = dateHistory;
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
    this.images = images;
    this.otherFiles = otherFiles;
  }
}
