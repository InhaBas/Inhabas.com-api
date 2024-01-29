package com.inhabas.api.domain.lecture.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureDetailDto {

  private Integer id;

  private String title;

  private MemberInfo chief;

  private List<MemberInfo> coInstructors;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime applyDeadLine;

  private String daysOfWeeks;

  private String place;

  private String introduction;

  private String curriculumDetails;

  private Integer participantsLimits;

  private Integer method;

  private LectureStatus state;

  private String rejectReason;

  private Boolean paid;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime created;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updated;

  @Builder
  public LectureDetailDto(
      Integer id,
      String title,
      MemberInfo chief,
      LocalDateTime applyDeadLine,
      String daysOfWeeks,
      String place,
      String introduction,
      String curriculumDetails,
      Integer participantsLimits,
      Integer method,
      LectureStatus state,
      String rejectReason,
      Boolean paid,
      LocalDateTime created,
      LocalDateTime updated) {
    this.id = id;
    this.title = title;
    this.chief = chief;
    this.coInstructors = List.of();
    this.applyDeadLine = applyDeadLine;
    this.daysOfWeeks = daysOfWeeks;
    this.place = place;
    this.introduction = introduction;
    this.curriculumDetails = curriculumDetails;
    this.participantsLimits = participantsLimits;
    this.method = method;
    this.state = state;
    this.rejectReason = rejectReason;
    this.paid = paid;
    this.created = created;
    this.updated = updated;
  }

  public LectureDetailDto setCoInstructors(List<MemberInfo> list) {
    this.coInstructors = list;
    return this;
  }

  @Getter
  public static final class MemberInfo {

    private final Integer id;

    private final String major;

    private final String name;

    public MemberInfo(Integer id, String major, String name) {
      this.id = id;
      this.major = major;
      this.name = name;
    }
  }
}
