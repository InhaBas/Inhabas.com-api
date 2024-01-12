package com.inhabas.api.domain.club.repository;

import com.inhabas.api.domain.board.domain.AlbumBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubActivityRepository extends JpaRepository<AlbumBoard, Long> {
}
