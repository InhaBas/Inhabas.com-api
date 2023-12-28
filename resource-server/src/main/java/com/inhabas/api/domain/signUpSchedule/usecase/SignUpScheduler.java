package com.inhabas.api.domain.signUpSchedule.usecase;

import com.inhabas.api.domain.signUpSchedule.dto.SignUpScheduleDto;


public interface SignUpScheduler {

    void updateSchedule(SignUpScheduleDto signUpScheduleDto);

    SignUpScheduleDto getSchedule();
}
