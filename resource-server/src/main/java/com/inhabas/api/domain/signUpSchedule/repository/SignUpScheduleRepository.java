package com.inhabas.api.domain.signUpSchedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.signUpSchedule.domain.entity.SignUpSchedule;

public interface SignUpScheduleRepository extends JpaRepository<SignUpSchedule, Integer> {}
