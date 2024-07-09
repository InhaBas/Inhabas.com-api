package com.inhabas.api.domain.contest.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

// 공모전 게시판 글 업데이트 및 저장
@Getter
@NoArgsConstructor()
public class SaveContestBoardDto {

  @NotNull(message = "공모전 분야를 선택해주세요.")
  private Long contestFieldId;

  @NotBlank(message = "제목을 입력하세요.")
  @Length(max = 100, message = "제목은 최대 100자입니다.")
  private String title;

  @NotBlank(message = "본문을 입력하세요.")
  private String content;

  @Length(max = 100, message = "100자 이내로 작성해주세요.")
  @NotBlank(message = "협회기관을 입력하세요.")
  private String association;

  @Length(max = 500, message = "500자 이내로 작성해주세요.")
  @NotBlank(message = "공모전 주제를 입력하세요.")
  private String topic;

  @NotNull(message = "공모전 모집 시작일을 등록해주세요.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateContestStart;

  @NotNull(message = "공모전 모집 마감일을 등록해주세요.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  @Future(message = "이미 모집기간이 종료된 공모전은 등록할 수 없습니다.")
  private LocalDateTime dateContestEnd;

  @NotNull
  @Size(min = 1)
  private List<String> files = new ArrayList<>();

  @Builder
  public SaveContestBoardDto(
      Long contestFieldId,
      String title,
      String content,
      String association,
      String topic,
      LocalDateTime dateContestStart,
      LocalDateTime dateContestEnd,
      List<String> files) {

    this.contestFieldId = contestFieldId;
    this.title = title;
    this.content = content;
    this.association = association;
    this.topic = topic;
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
    this.files = files == null ? new ArrayList<>() : files;
  }
}
