package com.inhabas.api.domain.lecture.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.lecture.LectureCannotModifiableException;
import com.inhabas.api.domain.lecture.domain.converter.LectureStatusConverter;
import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "lecture")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  @Length(max = 100)
  private String title;

  @Embedded
  @AttributeOverride(
      name = "id",
      column = @Column(name = "chief", nullable = false, updatable = false))
  private StudentId chief;

  @Convert(converter = LectureStatusConverter.class)
  @NotNull
  private LectureStatus status;

  @NotNull private LocalDateTime applyDeadline;

  @NotBlank
  @Length(max = 20)
  private String daysOfWeek;

  @NotNull
  @Length(max = 300)
  private String place;

  @NotNull
  @Length(max = 300)
  private String introduction;

  @NotNull
  @Length(max = 16777215)
  @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
  private String curriculumDetails;

  private int participantsLimits;

  @Column(columnDefinition = "TINYINT", nullable = false)
  private Integer method;

  @Length(max = 200)
  private String rejectReason;

  @Column(columnDefinition = "TINYINT", nullable = false)
  private Boolean paid;

  @Builder
  public Lecture(
      String title,
      StudentId chief,
      LocalDateTime applyDeadline,
      String daysOfWeek,
      String place,
      String introduction,
      String curriculumDetails,
      int participantsLimits,
      Integer method) {
    this.title = title;
    this.chief = chief;
    this.status = LectureStatus.WAITING;
    this.applyDeadline = applyDeadline;
    this.daysOfWeek = daysOfWeek;
    this.place = place;
    this.introduction = introduction;
    this.curriculumDetails = curriculumDetails;
    this.participantsLimits = participantsLimits;
    this.method = method;
    this.rejectReason = null;
    this.paid = false;
  }

  public void update(
      StudentId studentId,
      String title,
      LocalDateTime applyDeadline,
      String daysOfWeek,
      String place,
      String introduction,
      String curriculumDetails,
      int participantsLimits,
      Integer method) {

    if (this.id == null) throw new EntityNotFoundException("생성되지 않은 엔티티는 수정할 수 없습니다.");

    if (notModifiableBy(studentId)) throw new LectureCannotModifiableException();

    this.title = title;
    this.applyDeadline = applyDeadline;
    this.daysOfWeek = daysOfWeek;
    this.place = place;
    this.introduction = introduction;
    this.curriculumDetails = curriculumDetails;
    this.participantsLimits = participantsLimits;
    this.method = method;
  }

  public boolean notModifiableBy(StudentId studentId) {
    return !this.chief.equals(studentId);
  }

  public boolean isHeldBy(StudentId studentId) {
    return this.chief.equals(studentId);
  }

  /**
   * 개설 신청된 강의를 승인 또는 거절하는 함수
   *
   * @param status {@code LectureStatus.PROGRESSING}, {@code LectureStatus.DENIED} 둘 중 하나만 가능
   * @param rejectReason null 이어도 된다.
   */
  public void approveOrDeny(LectureStatus status, String rejectReason) {

    if (status == null) throw new IllegalArgumentException("status 값은 null 일 수 없습니다.");
    else if (status == LectureStatus.TERMINATED)
      throw new IllegalArgumentException("강제로 강의를 종료시킬 수 없습니다.");
    else if (status == LectureStatus.WAITING)
      throw new IllegalArgumentException("강의를 대기 상태롤 돌릴 수 없습니다.");

    this.status = status;
    this.rejectReason = rejectReason;
  }

  /**
   * 이미 진행된 강의는 삭제할 수 없다.
   *
   * @return 승인 대기 상태 또는 거절 상태라면 true
   */
  public boolean canBeDeleted() {

    return status == LectureStatus.WAITING || status == LectureStatus.DENIED;
  }
}
