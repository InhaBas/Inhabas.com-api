package com.inhabas.api.domain.normalBoard.repository;

import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalBoardRepository
    extends JpaRepository<NormalBoard, Long>, NormalBoardRepositoryCustom {}
