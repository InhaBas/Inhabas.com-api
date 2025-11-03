package com.inhabas.api.domain.contest.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;

// 공모전 게시판 단일 조회
@Getter
@NoArgsConstructor
public class ContestBoardDetailDto {

  @NotNull @Positive private Long id;

  @NotNull private Long contestFieldId;

  @NotBlank private String title;

  @NotBlank private String content;

  @NotNull private Long writerId;

  @NotBlank private String writerName;

  @NotBlank private String association;

  @NotBlank private String topic;

  @NotNull private FileDownloadDto thumbnail;

  @NotNull private List<FileDownloadDto> images;

  @NotNull private List<FileDownloadDto> otherFiles;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateContestStart;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateContestEnd;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  @Builder
  public ContestBoardDetailDto(
      Long id,
      Long contestFieldId,
      String title,
      String content,
      Long writerId,
      String writerName,
      String association,
      String topic,
      LocalDateTime dateContestStart,
      LocalDateTime dateContestEnd,
      LocalDateTime dateCreated,
      LocalDateTime dateUpdated,
      FileDownloadDto thumbnail,
      List<FileDownloadDto> images,
      List<FileDownloadDto> otherFiles) {
    this.id = id;
    this.contestFieldId = contestFieldId;
    this.title = title;
    this.content = content;
    this.writerId = writerId;
    this.writerName = writerName;
    this.association = association;
    this.topic = topic;
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
    this.thumbnail = thumbnail;
    this.images = images;
    this.otherFiles = otherFiles;
  }
}
