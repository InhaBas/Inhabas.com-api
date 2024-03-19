package com.inhabas.api.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.project.domain.ProjectBoard;

public interface ProjectBoardRepository
    extends JpaRepository<ProjectBoard, Long>, ProjectBoardRepositoryCustom {}
