package com.inhabas.api.domain.signup.domain;

import com.inhabas.api.domain.signup.dto.SignUpScheduleDto;


public interface SignUpScheduler {

    void updateSchedule(SignUpScheduleDto signUpScheduleDto);

    SignUpScheduleDto getSchedule();
}
