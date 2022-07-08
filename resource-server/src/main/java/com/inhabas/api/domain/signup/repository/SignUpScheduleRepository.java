package com.inhabas.api.domain.signup.repository;

import com.inhabas.api.domain.signup.domain.entity.SignUpSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpScheduleRepository extends JpaRepository<SignUpSchedule, Integer> {
}
