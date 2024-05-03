package com.inhabas.api.domain.contest.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;

// 공모전 게시판 전체 조회
@Getter
@NoArgsConstructor
public class ContestBoardDto {
  // 공모전 게시판 id
  private Long id;
  private Long contestFieldId;
  private String title;
  private String topic;
  private String association;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateContestStart;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateContestEnd;

  private Long dDay;

  private FileDownloadDto thumbnail;

  @Builder
  public ContestBoardDto(
      Long id,
      Long contestFieldId,
      String title,
      String topic,
      String association,
      LocalDateTime dateContestStart,
      LocalDateTime dateContestEnd,
      Long dDay,
      FileDownloadDto thumbnail) {
    this.id = id;
    this.contestFieldId = contestFieldId;
    this.title = title;
    this.topic = topic;
    this.association = association;
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
    this.dDay = dDay;
    this.thumbnail = thumbnail;
  }
}
