package com.inhabas.api.domain.board.repository;

import com.inhabas.api.domain.board.domain.NormalBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalBoardRepository extends JpaRepository<NormalBoard, Long>, NormalBoardRepositoryCustom {
}