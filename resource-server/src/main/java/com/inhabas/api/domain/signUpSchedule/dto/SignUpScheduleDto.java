package com.inhabas.api.domain.signUpSchedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.signUpSchedule.domain.entity.SignUpSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpScheduleDto {

    @NotNull
    private Long id;

    @NotNull @Positive
    private Integer generation;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupStartDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupEndDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime interviewStartDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime interviewEndDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resultAnnounceDate;

    public static SignUpScheduleDto from(SignUpSchedule signUpSchedule) {
        return new SignUpScheduleDto(
                signUpSchedule.getId(),
                signUpSchedule.getGeneration(),
                signUpSchedule.getSignupStartDate(),
                signUpSchedule.getSignupEndDate(),
                signUpSchedule.getInterviewStartDate(),
                signUpSchedule.getInterviewEndDate(),
                signUpSchedule.getResultAnnounceDate());
    }
}
