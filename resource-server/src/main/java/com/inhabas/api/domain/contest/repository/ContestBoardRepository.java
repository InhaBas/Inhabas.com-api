package com.inhabas.api.domain.contest.repository;

import com.inhabas.api.domain.contest.domain.ContestBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestBoardRepository
    extends JpaRepository<ContestBoard, Integer>, ContestBoardRepositoryCustom {}
