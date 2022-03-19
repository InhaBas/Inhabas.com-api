package com.inhabas.api.domain.signup;

import com.inhabas.api.dto.signUp.SignUpScheduleDto;
import com.inhabas.api.service.signup.SignUpScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
