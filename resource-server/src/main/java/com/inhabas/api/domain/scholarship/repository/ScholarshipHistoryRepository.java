package com.inhabas.api.domain.scholarship.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.scholarship.domain.ScholarshipHistory;

public interface ScholarshipHistoryRepository
    extends JpaRepository<ScholarshipHistory, Long>, ScholarshipHistoryRepositoryCustom {}
