package com.inhabas.api.domain.contest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.contest.domain.ContestField;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;

// 공모전 게시판 단일 조회
@Getter
@NoArgsConstructor
public class ContestBoardDetailDto {

  @NotNull @Positive private Long id;

  @NotBlank private ContestField contestField;

  @NotBlank private String title;

  @NotBlank private String content;

  @NotBlank private String writerName;

  @NotBlank private String association;

  @NotBlank private String topic;

  @NotNull private List<FileDownloadDto> files;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate dateContestStart;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate dateContestEnd;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  @Builder
  public ContestBoardDetailDto(
      Long id,
      ContestField contestField,
      String title,
      String content,
      String writerName,
      String association,
      String topic,
      LocalDate dateContestStart,
      LocalDate dateContestEnd,
      LocalDateTime dateCreated,
      LocalDateTime dateUpdated,
      List<FileDownloadDto> files) {
    this.id = id;
    this.contestField = contestField;
    this.title = title;
    this.content = content;
    this.writerName = writerName;
    this.association = association;
    this.topic = topic;
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
    this.files = files;
  }
}
