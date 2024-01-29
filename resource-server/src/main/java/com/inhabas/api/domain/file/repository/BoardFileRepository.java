package com.inhabas.api.domain.file.repository;

import com.inhabas.api.domain.file.domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {}
