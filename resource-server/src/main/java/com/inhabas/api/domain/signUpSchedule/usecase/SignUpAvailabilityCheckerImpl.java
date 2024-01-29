package com.inhabas.api.domain.signUpSchedule.usecase;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.inhabas.api.domain.signUpSchedule.dto.SignUpScheduleDto;

@Component
@RequiredArgsConstructor
public class SignUpAvailabilityCheckerImpl implements SignUpAvailabilityChecker {

  private final SignUpScheduler signUpScheduler;

  public boolean isAvailable() {
    SignUpScheduleDto schedule = signUpScheduler.getSchedule();
    LocalDateTime now = LocalDateTime.now();

    return schedule.getSignupStartDate().isBefore(now) && schedule.getSignupEndDate().isAfter(now);
  }
}
