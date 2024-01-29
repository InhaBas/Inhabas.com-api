package com.inhabas.api.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.file.domain.BoardFile;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {}
