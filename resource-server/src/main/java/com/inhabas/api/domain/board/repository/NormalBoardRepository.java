package com.inhabas.api.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.board.domain.NormalBoard;

public interface NormalBoardRepository
    extends JpaRepository<NormalBoard, Long>, NormalBoardRepositoryCustom {}
