package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.domain.lecture.dto.StudentListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentRepositoryCustom {

    Page<StudentListDto> searchStudents(Integer lectureId, Pageable pageable);
}
