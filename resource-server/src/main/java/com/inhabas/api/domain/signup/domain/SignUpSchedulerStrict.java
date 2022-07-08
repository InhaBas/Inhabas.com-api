package com.inhabas.api.domain.signup.domain;

import com.inhabas.api.domain.signup.domain.entity.SignUpSchedule;
import com.inhabas.api.domain.signup.repository.SignUpScheduleRepository;
import com.inhabas.api.domain.signup.dto.SignUpScheduleDto;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This Scheduler has a strict duty to leave only one signup-schedule.
 */
@Service
@RequiredArgsConstructor
public class SignUpSchedulerStrict implements SignUpScheduler {

    private final SignUpScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void updateSchedule(SignUpScheduleDto signUpScheduleDto) {
        this.clearDirtySchedules();
        SignUpSchedule signUpSchedule = this.getScheduleEntity();
        signUpSchedule.update(
                signUpScheduleDto.getGeneration(),
                signUpScheduleDto.getSignupStartDate(),
                signUpScheduleDto.getSignupEndDate(),
                signUpScheduleDto.getInterviewStartDate(),
                signUpScheduleDto.getInterviewEndDate(),
                signUpScheduleDto.getResultAnnounceDate());
        scheduleRepository.save(signUpSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public SignUpScheduleDto getSchedule() {
        SignUpSchedule signUpSchedule = this.getScheduleEntity();

        return new SignUpScheduleDto(
                signUpSchedule.getId(),
                signUpSchedule.getGeneration(),
                signUpSchedule.getSignupStartDate(),
                signUpSchedule.getSignupEndDate(),
                signUpSchedule.getInterviewStartDate(),
                signUpSchedule.getInterviewEndDate(),
                signUpSchedule.getResultAnnounceDate());
    }

    private SignUpSchedule getDefaultSchedule() {
        return new SignUpSchedule(999,
                LocalDateTime.of(1970, 1, 1, 0, 0, 0),
                LocalDateTime.of(1970, 1, 2, 0, 0, 0),
                LocalDateTime.of(1970, 1, 3, 0, 0, 0),
                LocalDateTime.of(1970, 1, 4, 0, 0, 0),
                LocalDateTime.of(1970, 1, 5, 0, 0, 0));
    }

    @Transactional
    protected SignUpSchedule createDefaultSchedule() {
        return scheduleRepository.save(this.getDefaultSchedule());
    }

    /**
     * A SignUpSchedule Entity will be guaranteed in any case.
     * @return existing ScheduleEntity otherwise DefaultScheduleEntity
     */
    @Transactional
    protected SignUpSchedule getScheduleEntity() {

        SignUpSchedule signUpSchedule;
        try {
            signUpSchedule = scheduleRepository.findAll().get(0);
        } catch (IndexOutOfBoundsException e) {
            signUpSchedule = this.createDefaultSchedule();
        }
        return signUpSchedule;
    }


    /**
     * In case of multiple SignUpSchedules, delete all the entities. <br>
     * Must create any Schedule entity after calling this method.
     */
    @Transactional
    protected void clearDirtySchedules() {
        if (scheduleRepository.findAll().size() > 1)
            scheduleRepository.deleteAll();
    }
}
