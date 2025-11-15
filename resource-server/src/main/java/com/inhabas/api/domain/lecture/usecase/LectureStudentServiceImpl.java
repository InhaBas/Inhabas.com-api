package com.inhabas.api.domain.lecture.usecase;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.domain.Student;
import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import com.inhabas.api.domain.lecture.dto.StudentListDto;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import com.inhabas.api.domain.lecture.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class LectureStudentServiceImpl implements LectureStudentService {

  private final StudentRepository studentRepository;
  private final LectureRepository lectureRepository;

  @Transactional
  @Override
  public void enroll(Integer lectureId, StudentId studentId) {

    Lecture lecture =
        lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);
    Student student = new Student(lecture, studentId);

    studentRepository.save(student);
  }

  @Transactional
  @Override
  public void changeStatusOfOneStudentByLecturer(
      Integer studentId, StudentId lecturerId, StudentStatus status, Integer lectureId) {

    studentRepository
        .findByLectureIdAndId(lectureId, studentId)
        .orElseThrow(EntityNotFoundException::new)
        .changeStatusByLecturer(status, lecturerId);
  }

  @Transactional
  @Override
  public void changeStatusOfStudentsByLecturer(
      Map<Integer, StudentStatus> list, StudentId instructorId, Integer lectureId) {

    Set<Integer> keySet = list.keySet();

    List<Student> students =
        studentRepository.findAllById(keySet, lectureId).stream()
            .map(student -> student.changeStatusByLecturer(list.get(student.getId()), instructorId))
            .collect(Collectors.toList());

    studentRepository.saveAll(students);
  }

  @Transactional
  @Override
  public void exitBySelf(Integer lectureId, StudentId studentId) {

    Student student =
        studentRepository
            .findByLectureIdAndStudentId(lectureId, studentId)
            .orElseThrow(EntityNotFoundException::new);

    student.exitLecture();
  }

  @Override
  public Page<StudentListDto> searchStudents(Integer lectureId, Pageable pageable) {

    return studentRepository.searchStudents(lectureId, pageable);
  }
}
