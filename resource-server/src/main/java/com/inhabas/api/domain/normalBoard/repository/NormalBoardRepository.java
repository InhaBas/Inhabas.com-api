package com.inhabas.api.domain.normalBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.normalBoard.domain.NormalBoard;

public interface NormalBoardRepository
    extends JpaRepository<NormalBoard, Long>, NormalBoardRepositoryCustom {}
