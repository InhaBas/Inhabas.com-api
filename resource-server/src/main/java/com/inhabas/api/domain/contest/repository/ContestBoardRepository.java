package com.inhabas.api.domain.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.contest.domain.ContestBoard;

public interface ContestBoardRepository
    extends JpaRepository<ContestBoard, Long>, ContestBoardRepositoryCustom {}
