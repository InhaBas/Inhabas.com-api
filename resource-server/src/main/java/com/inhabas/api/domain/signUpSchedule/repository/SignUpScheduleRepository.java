package com.inhabas.api.domain.signUpSchedule.repository;

import com.inhabas.api.domain.signUpSchedule.domain.entity.SignUpSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpScheduleRepository extends JpaRepository<SignUpSchedule, Integer> {
}
