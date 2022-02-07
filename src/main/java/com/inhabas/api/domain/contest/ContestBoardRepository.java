package com.inhabas.api.domain.contest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestBoardRepository extends JpaRepository<ContestBoard, Integer>, ContestBoardRepositoryCustom{
}