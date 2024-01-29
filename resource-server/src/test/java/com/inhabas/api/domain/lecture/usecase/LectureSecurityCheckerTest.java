package com.inhabas.api.domain.lecture.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.inhabas.api.domain.lecture.repository.LectureRepository;
import com.inhabas.api.web.LectureController;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

@NoSecureWebMvcTest(LectureController.class)
@Import(LectureSecurityChecker.class)
public class LectureSecurityCheckerTest {

  @Autowired private LectureSecurityChecker securityChecker;

  @MockBean private LectureRepository repository;

  @MockBean private LectureServiceImpl lectureService;

  @MockBean private LectureStudentServiceImpl studentService;

  //    @DisplayName("강의자만 접근할 수 있다.")
  //    @Test
  //    @WithMockJwtAuthenticationToken(memberId = 1L)
  //    public void instructorOnlyTest() {
  //
  //        //given
  //        StudentId lecturerId = new StudentId("12171652");
  //        Lecture lecture = Lecture.builder()
  //                .title("절권도 배우기")
  //                .chief(lecturerId)
  //                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
  //                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
  //                .daysOfWeek("월 금")
  //                .introduction("호신술을 배워보자")
  //                .method(1)
  //                .participantsLimits(30)
  //                .place("6호관 옥상")
  //                .build();
  //        ReflectionTestUtils.setField(lecture, "id", 1);
  //        given(repository.findById(any())).willReturn(Optional.of(lecture));
  //
  //        //when
  //        boolean result = securityChecker.instructorOnly(1);
  //
  //        //then
  //        assertTrue(result);
  //    }
  //
  //    @DisplayName("강의자가 아니어서 접근할 수 없다.")
  //    @Test
  //    @WithMockJwtAuthenticationToken(memberId = 11111111L)
  //    public void instructorOnlyDenyTest() {
  //
  //        //given
  //        StudentId lecturerId = new StudentId("12171652");
  //        Lecture lecture = Lecture.builder()
  //                .title("절권도 배우기")
  //                .chief(lecturerId)
  //                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
  //                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
  //                .daysOfWeek("월 금")
  //                .introduction("호신술을 배워보자")
  //                .method(1)
  //                .participantsLimits(30)
  //                .place("6호관 옥상")
  //                .build();
  //        ReflectionTestUtils.setField(lecture, "id", 1);
  //        given(repository.findById(any())).willReturn(Optional.of(lecture));
  //
  //        //when
  //        boolean result = securityChecker.instructorOnly(1);
  //
  //        //then
  //        assertFalse(result);
  //    }

}
