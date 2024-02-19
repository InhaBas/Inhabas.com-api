package com.inhabas.api.domain.lecture.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.lecture.domain.Lecture;

public interface LectureRepository
    extends JpaRepository<Lecture, Integer>, LectureRepositoryCustom {}
