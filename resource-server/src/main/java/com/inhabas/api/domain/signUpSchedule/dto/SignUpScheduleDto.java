package com.inhabas.api.domain.signUpSchedule.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.signUpSchedule.domain.entity.SignUpSchedule;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpScheduleDto {

  @NotNull
  @Positive
  @Schema(defaultValue = "1")
  private Integer generation;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2023-11-01T00:00:00")
  private LocalDateTime signupStartDate;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime signupEndDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime interviewStartDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime interviewEndDate;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime resultAnnounceDate;

  public static SignUpScheduleDto from(SignUpSchedule signUpSchedule) {
    return new SignUpScheduleDto(
        signUpSchedule.getGeneration(),
        signUpSchedule.getSignupStartDate(),
        signUpSchedule.getSignupEndDate(),
        signUpSchedule.getInterviewStartDate(),
        signUpSchedule.getInterviewEndDate(),
        signUpSchedule.getResultAnnounceDate());
  }
}
