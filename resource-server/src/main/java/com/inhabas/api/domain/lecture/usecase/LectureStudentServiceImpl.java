package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.domain.Student;
import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import com.inhabas.api.domain.lecture.dto.StudentListDto;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import com.inhabas.api.domain.lecture.repository.StudentRepository;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureStudentServiceImpl implements LectureStudentService {

    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    @Override
    public void enroll(Integer lectureId, MemberId memberId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);
        Student student = new Student(lecture, memberId);

        studentRepository.save(student);
    }

    @Transactional
    @Override
    public void changeStatusOfOneStudentByLecturer(Integer studentId, MemberId lecturerId, StudentStatus status, Integer lectureId) {

        studentRepository.findByLectureIdAndId(lectureId, studentId)
                .orElseThrow(EntityNotFoundException::new)
                .changeStatusByLecturer(status, lecturerId);
    }


    @Transactional
    @Override
    public void changeStatusOfStudentsByLecturer(Map<Integer, StudentStatus> list, MemberId instructorId, Integer lectureId) {

        Set<Integer> keySet = list.keySet();

        List<Student> students = studentRepository.findAllById(keySet, lectureId)
                .stream()
                .map(student -> student.changeStatusByLecturer(list.get(student.getId()), instructorId))
                .collect(Collectors.toList());

        studentRepository.saveAll(students);
    }


    @Transactional
    @Override
    public void exitBySelf(Integer lectureId, MemberId studentId) {

        Student student = studentRepository.findByLectureIdAndMemberId(lectureId, studentId)
                .orElseThrow(EntityNotFoundException::new);

        student.exitLecture();
    }


    @Override
    public Page<StudentListDto> searchStudents(Integer lectureId, Pageable pageable) {
        return null;
    }
}
