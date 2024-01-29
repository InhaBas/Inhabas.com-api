package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.lecture.domain.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository
    extends JpaRepository<Student, Integer>, StudentRepositoryCustom {

  Optional<Student> findByLectureIdAndStudentId(Integer lectureId, StudentId studentId);

  Optional<Student> findByLectureIdAndId(Integer lectureId, Integer sid);

  @Query("select s from Student s where s.id in :integers and s.lecture.id = :lectureId ")
  List<Student> findAllById(Iterable<Integer> integers, Integer lectureId);
}
