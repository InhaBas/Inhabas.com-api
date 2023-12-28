package com.inhabas.api.domain.signUpSchedule.domain.entity;

import com.inhabas.api.domain.signUpSchedule.domain.exception.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class SignUpScheduleTest {

    @DisplayName("스케줄 엔티티를 생성한다.")
    @Test
    public void createSignUpScheduleEntityTest() {
        //given
        Integer generation = 1;
        LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
        LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
        LocalDateTime interviewStart = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
        LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 4, 0, 0, 0);
        LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 5, 0, 0, 0);

        //when
        SignUpSchedule schedule = new SignUpSchedule(generation, signUpStart, signUpEnd, interviewStart, interviewEnd, resultAnnounce);

        //then
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
        //given
        Integer generation = 1;
        LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
        LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
        LocalDateTime interviewStart = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
        LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 4, 0, 0, 0);
        LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 5, 0, 0, 0);

        //when
        InvalidDateException invalidDateException = assertThrows(InvalidDateException.class,
                () -> new SignUpSchedule(generation, signUpStart, signUpEnd, interviewStart, interviewEnd, resultAnnounce));

        //then
        assertThat(invalidDateException.getMessage()).isEqualTo("등록 마감일이 시작일보다 앞설 수 없습니다.");
    }

    @DisplayName("면접 날짜가 잘못된 스케줄 엔티티 생성을 시도한다.")
    @Test
    public void invalidInterviewScheduleTest() {
        //given
        Integer generation = 1;
        LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 1, 0, 0, 0);
        LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
        LocalDateTime interviewStart = LocalDateTime.of(1000, 1, 4, 0, 0, 0);
        LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
        LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 5, 0, 0, 0);

        //when
        InvalidDateException invalidDateException = assertThrows(InvalidDateException.class,
                () -> new SignUpSchedule(generation, signUpStart, signUpEnd, interviewStart, interviewEnd, resultAnnounce));

        //then
        assertThat(invalidDateException.getMessage()).isEqualTo("면접 마감일이 시작일보다 앞설 수 없습니다.");
    }

    @DisplayName("결과 공고 날짜가 잘못된 스케줄 엔티티 생성을 시도한다.")
    @Test
    public void invalidAnnounceScheduleTest() {
        //given
        Integer generation = 1;
        LocalDateTime signUpStart = LocalDateTime.of(1000, 1, 2, 0, 0, 0);
        LocalDateTime signUpEnd = LocalDateTime.of(1000, 1, 3, 0, 0, 0);
        LocalDateTime interviewStart = LocalDateTime.of(1000, 1, 4, 0, 0, 0);
        LocalDateTime interviewEnd = LocalDateTime.of(1000, 1, 5, 0, 0, 0);
        LocalDateTime resultAnnounce = LocalDateTime.of(1000, 1, 1, 0, 0, 0);

        //when
        InvalidDateException invalidDateException = assertThrows(InvalidDateException.class,
                () -> new SignUpSchedule(generation, signUpStart, signUpEnd, interviewStart, interviewEnd, resultAnnounce));

        //then
        assertThat(invalidDateException.getMessage()).isEqualTo("결과 발표일이 면접 마감일보다 앞설 수 없습니다.");
    }



}
