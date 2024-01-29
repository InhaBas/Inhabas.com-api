package com.inhabas.api.domain.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.board.domain.AlbumBoard;

public interface ClubActivityRepository extends JpaRepository<AlbumBoard, Long> {}
