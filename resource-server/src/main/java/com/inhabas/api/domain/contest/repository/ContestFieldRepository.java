package com.inhabas.api.domain.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.contest.domain.ContestField;

public interface ContestFieldRepository extends JpaRepository<ContestField, Long> {}
