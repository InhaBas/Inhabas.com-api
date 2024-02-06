package com.inhabas.api.domain.contest.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.file.dto.FileDownloadDto;

// 공모전 게시판 전체 조회
@Getter
@NoArgsConstructor
public class ContestBoardDto {
  // 공모전 게시판 id
  private Long id;
  private String title;
  private String topic;
  private String association;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate dateContestStart;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate dateContestEnd;

  private FileDownloadDto thumbnail;

  @Builder
  public ContestBoardDto(
      Long id,
      String title,
      String topic,
      String association,
      LocalDate dateContestStart,
      LocalDate dateContestEnd,
      FileDownloadDto thumbnail) {
    this.id = id;
    this.title = title;
    this.topic = topic;
    this.association = association;
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
    this.thumbnail = thumbnail;
  }
}
