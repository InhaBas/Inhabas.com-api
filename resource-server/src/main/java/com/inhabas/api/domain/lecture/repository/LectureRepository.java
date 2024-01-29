package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.domain.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository
    extends JpaRepository<Lecture, Integer>, LectureRepositoryCustom {}
