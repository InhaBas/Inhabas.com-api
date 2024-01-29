package com.inhabas.api.domain.lecture.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureListDto {

  private Integer lectureId;

  private String title;

  private Integer chiefId;

  private String introduction;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime applyDeadline;

  private LectureStatus status;

  private Integer method;

  private Integer participantsLimits;

  private Integer theNumberOfCurrentParticipants;

  @Builder
  public LectureListDto(
      Integer lectureId,
      String title,
      Integer chiefId,
      String introduction,
      LocalDateTime applyDeadline,
      LectureStatus status,
      Integer method,
      Integer participantsLimits,
      Integer theNumberOfCurrentParticipants) {
    this.lectureId = lectureId;
    this.title = title;
    this.chiefId = chiefId;
    this.introduction = introduction;
    this.applyDeadline = applyDeadline;
    this.status = status;
    this.method = method;
    this.participantsLimits = participantsLimits;
    this.theNumberOfCurrentParticipants = theNumberOfCurrentParticipants;
  }
}
