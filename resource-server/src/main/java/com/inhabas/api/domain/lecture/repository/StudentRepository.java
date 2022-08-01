package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.domain.lecture.domain.Student;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByLectureIdAndMemberId(Integer lectureId, MemberId studentId);
}
