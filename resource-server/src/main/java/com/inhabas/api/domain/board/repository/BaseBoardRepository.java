package com.inhabas.api.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.board.domain.BaseBoard;

public interface BaseBoardRepository extends JpaRepository<BaseBoard, Long>, BaseBoardRepositoryCustom {}
