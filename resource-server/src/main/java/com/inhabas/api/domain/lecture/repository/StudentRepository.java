package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.domain.lecture.domain.Student;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer>, StudentRepositoryCustom {

    Optional<Student> findByLectureIdAndMemberId(Integer lectureId, MemberId studentId);

    Optional<Student> findByLectureIdAndId(Integer lectureId, Integer sid);

    @Query("select s from Student s where s.id in :integers and s.lecture.id = :lectureId ")
    List<Student> findAllById(Iterable<Integer> integers, Integer lectureId);
}
