package com.inhabas.api.domain.contest.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@AllArgsConstructor
public class ListContestBoardDto {
  private String title;
  private String topic;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate start;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate deadline;
}
