package com.inhabas.api.domain.signUpSchedule.domain.entity;

import com.inhabas.api.domain.signUpSchedule.InvalidDateException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * This entity exists only one in database table. No reason for many entities. <br>
 * It's nothing but messy and confusing to create multiple signUpSchedule. <br>
 * Be cautious not to make multi schedule or remove all the schedules. Do offer really necessary api.
 */
@Entity @Getter
@Table(name = "signup_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpSchedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "generation", nullable = false)
    private Integer generation;

    @Column(name = "signup_start", nullable = false)
    private LocalDateTime signupStartDate;

    @Column(name = "signup_end", nullable = false)
    private LocalDateTime signupEndDate;

    @Column(name = "interview_start", nullable = false)
    private LocalDateTime interviewStartDate;

    @Column(name = "interview_end", nullable = false)
    private LocalDateTime interviewEndDate;

    @Column(name = "result_announce_date", nullable = false)
    private LocalDateTime resultAnnounceDate;

    public SignUpSchedule(Integer generation, LocalDateTime signupStartDate, LocalDateTime signupEndDate,
                          LocalDateTime interviewStartDate, LocalDateTime interviewEndDate, LocalDateTime resultAnnounceDate) {

        this.update(generation, signupStartDate, signupEndDate, interviewStartDate, interviewEndDate, resultAnnounceDate);
    }

    public void update(Integer generation, LocalDateTime signupStartDate, LocalDateTime signupEndDate,
                       LocalDateTime interviewStartDate, LocalDateTime interviewEndDate, LocalDateTime resultAnnounceDate) {
        this.generation = generation;
        setSignUpDate(signupStartDate, signupEndDate);
        setInterviewDate(interviewStartDate, interviewEndDate);
        setResultAnnounceDate(signupEndDate, interviewEndDate, resultAnnounceDate);
    }

    private void setSignUpDate(LocalDateTime signupStartDate, LocalDateTime signupEndDate) {
        if (signupEndDate.isAfter(signupStartDate)) {
            this.signupStartDate = signupStartDate;
            this.signupEndDate = signupEndDate;
        }
        else {
            throw new InvalidDateException("등록 마감일이 시작일보다 앞설 수 없습니다.");
        }
    }

    private void setInterviewDate(LocalDateTime interviewStartDate, LocalDateTime interviewEndDate) {
        if (interviewEndDate.isAfter(interviewStartDate)) {
            this.interviewStartDate = interviewStartDate;
            this.interviewEndDate = interviewEndDate;
        }
        else {
            throw new InvalidDateException("면접 마감일이 시작일보다 앞설 수 없습니다.");
        }
    }

    private void setResultAnnounceDate(
            LocalDateTime signupEndDate, LocalDateTime interviewEndDate, LocalDateTime resultAnnounceDate) {

        if (resultAnnounceDate.isAfter(signupEndDate) && resultAnnounceDate.isAfter(interviewEndDate)) {
            this.resultAnnounceDate = resultAnnounceDate;
        }
        else {
            throw new InvalidDateException("결과 발표일을 다시 확인해주세요!");
        }
    }
}
