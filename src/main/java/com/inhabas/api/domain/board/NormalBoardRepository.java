package com.inhabas.api.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NormalBoardRepository extends JpaRepository<NormalBoard, Integer>, NormalBoardRepositoryCustom {
}

