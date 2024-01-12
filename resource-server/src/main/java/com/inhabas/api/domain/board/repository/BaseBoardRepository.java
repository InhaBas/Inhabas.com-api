package com.inhabas.api.domain.board.repository;

import com.inhabas.api.domain.board.domain.BaseBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseBoardRepository extends JpaRepository<BaseBoard, Long> {
}
