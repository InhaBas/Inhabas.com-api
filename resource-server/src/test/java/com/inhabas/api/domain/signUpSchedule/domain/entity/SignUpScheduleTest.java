package com.inhabas.api.domain.signUpSchedule.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import com.inhabas.api.domain.signUpSchedule.exception.InvalidDateException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SignUpScheduleTest {

  @DisplayName("스케줄 엔티티를 생성한다.")
  @Test
  public void createSignUpScheduleEntityTest() {
    // given
    Integer generation = 1;
    LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
    LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
    LocalDateTime interviewStart = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
    LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 4, 0, 0, 0);
    LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 5, 0, 0, 0);

    // when
    SignUpSchedule schedule =
        new SignUpSchedule(
            generation, signUpStart, signUpEnd, interviewStart, interviewEnd, resultAnnounce);

    // then
    assertThat(schedule.getGeneration()).isEqualTo(generation);
    assertThat(schedule.getSignupStartDate()).isEqualTo(signUpStart);
    assertThat(schedule.getSignupEndDate()).isEqualTo(signUpEnd);
    assertThat(schedule.getInterviewStartDate()).isEqualTo(interviewStart);
    assertThat(schedule.getInterviewEndDate()).isEqualTo(interviewEnd);
    assertThat(schedule.getResultAnnounceDate()).isEqualTo(resultAnnounce);
  }

  @DisplayName("등록 날짜가 잘못된 스케줄 엔티티 생성을 시도한다.")
  @Test
  public void invalidSignUpScheduleTest() {
    // given
    Integer generation = 1;
    LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
    LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
    LocalDateTime interviewStart = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
    LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 4, 0, 0, 0);
    LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 5, 0, 0, 0);

    // when
    InvalidDateException invalidDateException =
        assertThrows(
            InvalidDateException.class,
            () ->
                new SignUpSchedule(
                    generation,
                    signUpStart,
                    signUpEnd,
                    interviewStart,
                    interviewEnd,
                    resultAnnounce));

    // then
    assertThat(invalidDateException.getMessage()).isEqualTo("등록 마감일이 시작일보다 앞설 수 없습니다.");
  }

  @DisplayName("면접 날짜가 잘못된 스케줄 엔티티 생성을 시도한다.")
  @Test
  public void invalidInterviewScheduleTest() {
    // given
    Integer generation = 1;
    LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
    LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
    LocalDateTime interviewStart = LocalDateTime.of(1000, 1, 4, 0, 0, 0);
    LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
    LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 5, 0, 0, 0);

    // when
    InvalidDateException invalidDateException =
        assertThrows(
            InvalidDateException.class,
            () ->
                new SignUpSchedule(
                    generation,
                    signUpStart,
                    signUpEnd,
                    interviewStart,
                    interviewEnd,
                    resultAnnounce));

    // then
    assertThat(invalidDateException.getMessage()).isEqualTo("면접 마감일이 시작일보다 앞설 수 없습니다.");
  }

  @DisplayName("면접 날짜가 누락된 스케줄 엔티티 생성을 시도한다.")
  @Test
  public void incompleteInterviewScheduleTest() {
    // given
    Integer generation = 1;
    LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
    LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
    LocalDateTime interviewStart = null;
    LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
    LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 5, 0, 0, 0);

    // when
    InvalidDateException invalidDateException =
        assertThrows(
            InvalidDateException.class,
            () ->
                new SignUpSchedule(
                    generation,
                    signUpStart,
                    signUpEnd,
                    interviewStart,
                    interviewEnd,
                    resultAnnounce));

    // then
    assertThat(invalidDateException.getMessage()).isEqualTo("면접 시작일과 종료일 중 하나만 설정되어 있습니다.");
  }

  @DisplayName("결과 공고 날짜가 잘못된 스케줄 엔티티 생성을 시도한다.")
  @Test
  public void invalidAnnounceScheduleTest() {
    // given
    Integer generation = 1;
    LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
    LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
    LocalDateTime interviewStart = LocalDateTime.of(1000, 1, 4, 0, 0, 0);
    LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 5, 0, 0, 0);
    LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 1, 0, 0, 0);

    // when
    InvalidDateException invalidDateException =
        assertThrows(
            InvalidDateException.class,
            () ->
                new SignUpSchedule(
                    generation,
                    signUpStart,
                    signUpEnd,
                    interviewStart,
                    interviewEnd,
                    resultAnnounce));

    // then
    assertThat(invalidDateException.getMessage())
        .isEqualTo("결과 발표일이 면접 마감일 또는 모집 마감일보다 앞설 수 없습니다.");
  }

  @DisplayName("면접 날짜 둘 다 누락되어있으면 결과 공고일 검사는 모집일정을 기준으로만 판단한다.")
  @Test
  public void AnnounceScheduleTestWhenInterviewDateIsIncomplete() {
    // given
    Integer generation = 1;
    LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
    LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
    LocalDateTime interviewStart = null;
    LocalDateTime interviewEnd = null;
    LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 4, 0, 0, 0);

    // when & then
    assertThatCode(
            () ->
                new SignUpSchedule(
                    generation,
                    signUpStart,
                    signUpEnd,
                    interviewStart,
                    interviewEnd,
                    resultAnnounce))
        .doesNotThrowAnyException();
  }
}
