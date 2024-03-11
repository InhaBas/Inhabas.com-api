package com.inhabas.api.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.normalBoard.domain.NormalBoard;

public interface ProjectBoardRepository
    extends JpaRepository<NormalBoard, Long>, ProjectBoardRepositoryCustom {}
