package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.domain.Student;
import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import com.inhabas.api.domain.lecture.repository.StudentRepository;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class LectureStudentServiceMockTest {

    @InjectMocks
    private LectureStudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private LectureRepository lectureRepository;

    @DisplayName("수강생이 강의를 신청한다.")
    @Test
    public void enrollTest() {

        //given
        MemberId lecturerId = new MemberId(12171652);
        Lecture lecture = Lecture.builder()
                .title("절권도 배우기")
                .chief(lecturerId)
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(lecture, "id", 1);
        given(lectureRepository.findById(any())).willReturn(Optional.of(lecture));
        given(studentRepository.save(any())).willReturn(null);

        //when
        studentService.enroll(1, new MemberId(12212242));

        //then
        then(studentRepository).should(times(1)).save(any());
        then(lectureRepository).should(times(1)).findById(any());
    }


    @DisplayName("강의자를 수강생으로 등록할 수는 없다.")
    @Test
    public void cannotEnrollLecturerTest() {

        //given
        MemberId lecturerId = new MemberId(12171652);
        Lecture lecture = Lecture.builder()
                .title("절권도 배우기")
                .chief(lecturerId)
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(lecture, "id", 1);
        given(lectureRepository.findById(any())).willReturn(Optional.of(lecture));

        //when
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->studentService.enroll(1, lecturerId));
    }

    @DisplayName("수강생이 강의를 탈퇴한다.")
    @Test
    public void exitLecture() {

        //given
        MemberId lecturerId = new MemberId(12171652);
        Lecture lecture = Lecture.builder()
                .title("절권도 배우기")
                .chief(lecturerId)
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(lecture, "id", 1);

        MemberId studentId = new MemberId(11112222);
        Student student = new Student(lecture, studentId);
        given(studentRepository.findByLectureIdAndMemberId(any(), any())).willReturn(Optional.of(student));

        //when
        studentService.exitBySelf(1, studentId);

        //then
        StudentStatus status = (StudentStatus) ReflectionTestUtils.getField(student, "status");
        assertThat(status).isEqualTo(StudentStatus.EXIT);
        then(studentRepository).should(times(1)).findByLectureIdAndMemberId(any(), any());
    }
}