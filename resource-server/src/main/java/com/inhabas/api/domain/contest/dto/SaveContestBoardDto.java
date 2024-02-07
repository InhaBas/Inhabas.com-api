package com.inhabas.api.domain.contest.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import org.hibernate.validator.constraints.Length;

// 공모전 게시판 글 업데이트 및 저장
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveContestBoardDto {

  private ContestType contestType;

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
  private LocalDate dateContestStart;

  @NotNull(message = "공모전 모집 마감일을 등록해주세요.")
  @Future(message = "이미 모집기간이 종료된 공모전은 등록할 수 없습니다.")
  private LocalDate dateContestEnd;

  private List<MultipartFile> files;

  @Builder
  public SaveContestBoardDto(
      String title,
      String content,
      String association,
      String topic,
      LocalDate dateContestStart,
      LocalDate dateContestEnd,
      List<MultipartFile> files) {
    this.title = title;
    this.content = content;
    this.association = association;
    this.topic = topic;
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
    this.files = (files != null) ? files : new ArrayList<>();
  }
}
