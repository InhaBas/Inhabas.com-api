package com.inhabas.api.domain.signUpSchedule.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.domain.signUpSchedule.exception.InvalidDateException;

/**
 * This entity exists only one in database table. No reason for many entities. <br>
 * It's nothing but messy and confusing to create multiple signUpSchedule. <br>
 * Be cautious not to make multi schedule or remove all the schedules. Do offer really necessary
 * api.
 */
@Entity
@Getter
@Table(name = "SIGNUP_SCHEDULE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpSchedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "GENERATION", nullable = false)
  private Integer generation;

  @Column(name = "SIGNUP_START", nullable = false)
  private LocalDateTime signupStartDate;

  @Column(name = "SIGNUP_END", nullable = false)
  private LocalDateTime signupEndDate;

  @Column(name = "INTERVIEW_START", nullable = true)
  private LocalDateTime interviewStartDate;

  @Column(name = "INTERVIEW_END", nullable = true)
  private LocalDateTime interviewEndDate;

  @Column(name = "RESULT_ANNOUNCE_DATE", nullable = false)
  private LocalDateTime resultAnnounceDate;

  public SignUpSchedule(
      Integer generation,
      LocalDateTime signupStartDate,
      LocalDateTime signupEndDate,
      LocalDateTime interviewStartDate,
      LocalDateTime interviewEndDate,
      LocalDateTime resultAnnounceDate) {

    this.update(
        generation,
        signupStartDate,
        signupEndDate,
        interviewStartDate,
        interviewEndDate,
        resultAnnounceDate);
  }

  public void update(
      Integer generation,
      LocalDateTime signupStartDate,
      LocalDateTime signupEndDate,
      LocalDateTime interviewStartDate,
      LocalDateTime interviewEndDate,
      LocalDateTime resultAnnounceDate) {
    this.generation = generation;
    setSignUpDate(signupStartDate, signupEndDate);
    setInterviewDate(interviewStartDate, interviewEndDate);
    setResultAnnounceDate(signupEndDate, interviewEndDate, resultAnnounceDate);
  }

  private void setSignUpDate(LocalDateTime signupStartDate, LocalDateTime signupEndDate) {
    if (signupEndDate.isAfter(signupStartDate)) {
      this.signupStartDate = signupStartDate;
      this.signupEndDate = signupEndDate;
    } else {
      throw new InvalidDateException(ErrorCode.INVALID_SIGNUP_DATE);
    }
  }

  private void setInterviewDate(LocalDateTime interviewStartDate, LocalDateTime interviewEndDate) {
    boolean isStartNull = interviewStartDate == null;
    boolean isEndNull = interviewEndDate == null;

    // 면접 시작일과 종료일 중 하나만 null인 경우 오류 발생
    if (isStartNull ^ isEndNull) {
      throw new InvalidDateException(ErrorCode.INCOMPLETE_INTERVIEW_DATE);
    }

    // 둘 다 null이 아니면, 종료일이 시작일 이후인지 검사
    if (!isStartNull && !isEndNull) {
      if (!interviewEndDate.isAfter(interviewStartDate)) {
        throw new InvalidDateException(ErrorCode.INVALID_INTERVIEW_DATE);
      }
    }

    // 모든 검사를 통과하면 필드 업데이트
    this.interviewStartDate = interviewStartDate;
    this.interviewEndDate = interviewEndDate;
  }

  private void setResultAnnounceDate(
      LocalDateTime signupEndDate,
      LocalDateTime interviewEndDate,
      LocalDateTime resultAnnounceDate) {

    // 결과 발표일은 항상 회원가입 종료일 이후여야 한다.
    if (!resultAnnounceDate.isAfter(signupEndDate)) {
      throw new InvalidDateException(ErrorCode.INVALID_ANNOUNCE_DATE);
    }

    // 면접 종료일이 null이 아닌 경우, 결과 발표일은 면접 종료일 이후여야 한다.
    if (interviewEndDate != null && !resultAnnounceDate.isAfter(interviewEndDate)) {
      throw new InvalidDateException(ErrorCode.INVALID_ANNOUNCE_DATE);
    }

    // 위의 조건들을 모두 통과하면, 결과 발표일을 설정한다.
    this.resultAnnounceDate = resultAnnounceDate;
  }
}
