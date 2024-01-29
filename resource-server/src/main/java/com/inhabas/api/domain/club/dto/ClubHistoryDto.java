package com.inhabas.api.domain.club.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.club.domain.ClubHistory;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class ClubHistoryDto {

  @NotNull @Positive private Long id;

  @NotNull private String title;

  @NotNull private String content;

  @NotNull @Positive private Long writerId;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2023-11-01T00:00:00")
  private LocalDateTime dateHistory;

  @Builder
  public ClubHistoryDto(
      Long id, String title, String content, Long writerId, LocalDateTime dateHistory) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.writerId = writerId;
    this.dateHistory = dateHistory;
  }

  public ClubHistoryDto(ClubHistory clubHistory) {
    this.id = clubHistory.getId();
    this.title = clubHistory.getTitle().getValue();
    this.content = clubHistory.getContent().getValue();
    this.writerId = clubHistory.getMember().getId();
    this.dateHistory = clubHistory.getDateHistory();
  }
}
