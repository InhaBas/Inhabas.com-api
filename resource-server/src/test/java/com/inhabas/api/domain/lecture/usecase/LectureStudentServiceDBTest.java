package com.inhabas.api.domain.lecture.usecase;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import com.inhabas.api.domain.lecture.repository.StudentRepository;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

@DefaultDataJpaTest
public class LectureStudentServiceDBTest {

  @Autowired private StudentRepository studentRepository;

  @Autowired private LectureRepository lectureRepository;

  @Autowired private EntityManager entityManager;

  private LectureStudentService studentService;

  private Member lecturer;
  private Member student;
  private Lecture lecture;

  //    @BeforeEach
  //    public void setUp() {
  //        studentService = new LectureStudentServiceImpl(studentRepository, lectureRepository);
  //
  //        lecturer = basicMember1();
  //        entityManager.persist(lecturer);
  //
  //        student = basicMember2();
  //        entityManager.persist(student);
  //
  //        lecture = lectureRepository.save(Lecture.builder()
  //                .title("절권도 배우기")
  //                .chief(lecturer.getId())
  //                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
  //                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
  //                .daysOfWeek("월 금")
  //                .introduction("호신술을 배워보자")
  //                .method(1)
  //                .participantsLimits(30)
  //                .place("6호관 옥상")
  //                .build());
  //    }

  //    @DisplayName("수강생 등록")
  //    @Test
  //    public void enrollTest() {
  //
  //        //given
  //        Integer id = (Integer) ReflectionTestUtils.getField(lecture, "id");
  //
  //        //when
  //        studentService.enroll(id, student.getId());
  //
  //        //then
  //        List<Student> students = studentRepository.findAll();
  //        Assertions.assertThat(students).hasSize(1);
  //    }

  //    @DisplayName("중복 등록 시 unique constraint 위반")
  //    @Test
  //    public void uniqueConstraintTest() {
  //
  //        //given
  //        Integer id = (Integer) ReflectionTestUtils.getField(lecture, "id");
  //        studentService.enroll(id, student.getId());
  //
  //        //when
  //        assertThrows(DataIntegrityViolationException.class,
  //                ()->studentService.enroll(id, student.getId()));
  //    }

}
