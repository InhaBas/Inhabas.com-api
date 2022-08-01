package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.domain.Student;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import com.inhabas.api.domain.lecture.repository.StudentRepository;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.inhabas.api.domain.member.domain.MemberTest.MEMBER1;
import static com.inhabas.api.domain.member.domain.MemberTest.MEMBER2;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DefaultDataJpaTest
public class LectureStudentServiceDBTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private EntityManager entityManager;

    private LectureStudentService studentService;

    private Member lecturer;
    private Member student;
    private Lecture lecture;

    @BeforeEach
    public void setUp() {
        studentService = new LectureStudentServiceImpl(studentRepository, lectureRepository);

        lecturer = MEMBER1();
        entityManager.persist(lecturer);

        student = MEMBER2();
        entityManager.persist(student);

        lecture = lectureRepository.save(Lecture.builder()
                .title("절권도 배우기")
                .chief(lecturer.getId())
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build());
    }

    @DisplayName("수강생 등록")
    @Test
    public void enrollTest() {

        //given
        Integer id = (Integer) ReflectionTestUtils.getField(lecture, "id");

        //when
        studentService.enroll(id, student.getId());

        //then
        List<Student> students = studentRepository.findAll();
        Assertions.assertThat(students).hasSize(1);
    }

    @DisplayName("중복 등록 시 unique constraint 위반")
    @Test
    public void uniqueConstraintTest() {

        //given
        Integer id = (Integer) ReflectionTestUtils.getField(lecture, "id");
        studentService.enroll(id, student.getId());

        //when
        assertThrows(DataIntegrityViolationException.class,
                ()->studentService.enroll(id, student.getId()));
    }


}
