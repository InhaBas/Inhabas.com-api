package com.inhabas.api.domain.scholarship.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.scholarship.domain.Scholarship;

public interface ScholarshipBoardRepository
    extends JpaRepository<Scholarship, Long>, ScholarshipBoardRepositoryCustom {}
