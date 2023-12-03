package com.inhabas.api.domain.signUpSchedule.domain.usecase;

import com.inhabas.api.domain.signUpSchedule.domain.entity.SignUpSchedule;
import com.inhabas.api.domain.signUpSchedule.dto.SignUpScheduleDto;
import com.inhabas.api.domain.signUpSchedule.repository.SignUpScheduleRepository;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DefaultDataJpaTest
@Import(SignUpSchedulerStrict.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StrictSignUpSchedulerTest {

    @Autowired
    SignUpSchedulerStrict signUpScheduler;

    @SpyBean
    SignUpScheduleRepository signUpScheduleRepository;

    @Autowired
    TestEntityManager entityManager;


    @DisplayName("스케줄이 하나도 없으면 default 스케줄을 생성한다.")
    @Test
    public void initializeScheduleTest() {
        //when
        SignUpScheduleDto schedule = signUpScheduler.getSchedule();

        //then
        assertThat(schedule).isNotNull();
        assertThat(schedule.getSignupStartDate().toString()).isEqualTo("1970-01-01T00:00");
        then(signUpScheduleRepository).should(times(1)).save(any());
        then(signUpScheduleRepository).should(times(1)).findAll();
    }

    @DisplayName("기존 스케줄을 불러온다.")
    @Test
    public void getScheduleTest() {
        //given
        SignUpSchedule existingSchedule = new SignUpSchedule(999,
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0, 0),
                LocalDateTime.of(2022, 1, 3, 0, 0, 0),
                LocalDateTime.of(2022, 1, 4, 0, 0, 0),
                LocalDateTime.of(2022, 1, 5, 0, 0, 0));
        existingSchedule = entityManager.persist(existingSchedule);

        //when
        SignUpScheduleDto schedule = signUpScheduler.getSchedule();

        //then
        assertThat(schedule).isNotNull();
        assertThat(schedule)
                .usingRecursiveComparison()
                .isEqualTo(SignUpScheduleDto.from(existingSchedule));
        then(signUpScheduleRepository).should(times(1)).findAll();
    }

    @DisplayName("기존의 스케줄을 변경한다.")
    @Test
    public void updateScheduleTest() {
        //given
        SignUpSchedule schedule = entityManager.persist(new SignUpSchedule(1,
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0, 0),
                LocalDateTime.of(2022, 1, 3, 0, 0, 0),
                LocalDateTime.of(2022, 1, 4, 0, 0, 0),
                LocalDateTime.of(2022, 1, 5, 0, 0, 0)));

        SignUpScheduleDto signUpScheduleDto = SignUpScheduleDto.from(schedule);

        //when
        signUpScheduler.updateSchedule(signUpScheduleDto);

        //then
        then(signUpScheduleRepository).should(times(1)).save(any(SignUpSchedule.class));
        then(signUpScheduleRepository).should(times(2)).findAll();
    }

    @DisplayName("아무 데이터가 없을 때, 디폴트 값을 생성한 후 변경한다.")
    @Test
    public void updateBeforeInitializeScheduleTest() {
        //given
        SignUpScheduleDto signUpScheduleDto = new SignUpScheduleDto(
                1, LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0, 0),
                LocalDateTime.of(2022, 1, 3, 0, 0, 0),
                LocalDateTime.of(2022, 1, 4, 0, 0, 0),
                LocalDateTime.of(2022, 1, 5, 0, 0, 0)
        );

        //when
        signUpScheduler.updateSchedule(signUpScheduleDto);

        //then
        then(signUpScheduleRepository).should(times(2)).save(any(SignUpSchedule.class));
        then(signUpScheduleRepository).should(times(2)).findAll();
    }

    @DisplayName("스케줄이 여러 개 있었을 때, 기존 정보를 모두 제거한 후 변경한다.")
    @Test
    public void updateBeforeDeleteAllScheduleTest() {
        //given
        SignUpSchedule schedule = new SignUpSchedule(1,
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0, 0),
                LocalDateTime.of(2022, 1, 3, 0, 0, 0),
                LocalDateTime.of(2022, 1, 4, 0, 0, 0),
                LocalDateTime.of(2022, 1, 5, 0, 0, 0));
        entityManager.persist(schedule);
        entityManager.persist(schedule);
        entityManager.persist(schedule);

        SignUpScheduleDto signUpScheduleDto = SignUpScheduleDto.from(schedule);

        //when
        signUpScheduler.updateSchedule(signUpScheduleDto);

        //then
        then(signUpScheduleRepository).should(times(1)).save(any(SignUpSchedule.class));
        then(signUpScheduleRepository).should(times(2)).findAll();
    }
}
