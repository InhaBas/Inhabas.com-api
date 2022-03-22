package com.inhabas.api.service.signup;

import com.inhabas.api.dto.signUp.SignUpScheduleDto;


public interface SignUpScheduler {

    void updateSchedule(SignUpScheduleDto signUpScheduleDto);

    SignUpScheduleDto getSchedule();
}
