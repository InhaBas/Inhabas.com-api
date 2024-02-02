package com.inhabas.api.domain.lecture.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inhabas.api.domain.lecture.dto.StudentListDto;

public interface StudentRepositoryCustom {

  Page<StudentListDto> searchStudents(Integer lectureId, Pageable pageable);
}
