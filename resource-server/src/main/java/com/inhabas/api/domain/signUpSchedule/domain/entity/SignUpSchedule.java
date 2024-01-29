package com.inhabas.api.domain.signUpSchedule.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

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

  @Column(name = "INTERVIEW_START", nullable = false)
  private LocalDateTime interviewStartDate;

  @Column(name = "INTERVIEW_END", nullable = false)
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
    if (interviewEndDate.isAfter(interviewStartDate)) {
      this.interviewStartDate = interviewStartDate;
      this.interviewEndDate = interviewEndDate;
    } else {
      throw new InvalidDateException(ErrorCode.INVALID_INTERVIEW_DATE);
    }
  }

  private void setResultAnnounceDate(
      LocalDateTime signupEndDate,
      LocalDateTime interviewEndDate,
      LocalDateTime resultAnnounceDate) {

    if (resultAnnounceDate.isAfter(signupEndDate) && resultAnnounceDate.isAfter(interviewEndDate)) {
      this.resultAnnounceDate = resultAnnounceDate;
    } else {
      throw new InvalidDateException(ErrorCode.INVALID_ANNOUNCE_DATE);
    }
  }
}
