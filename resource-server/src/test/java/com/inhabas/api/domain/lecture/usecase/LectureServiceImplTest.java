package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.lecture.LectureCannotModifiableException;
import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;
import com.inhabas.api.domain.lecture.dto.LectureDetailDto;
import com.inhabas.api.domain.lecture.dto.LectureRegisterForm;
import com.inhabas.api.domain.lecture.dto.LectureUpdateForm;
import com.inhabas.api.domain.lecture.dto.LectureStatusUpdateRequest;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class LectureServiceImplTest {

    @InjectMocks
    private LectureServiceImpl service;

    @Mock
    private LectureRepository repository;

    @DisplayName("강의실 개설 글 작성")
    @Test
    public void createTest() {

        //given
        StudentId chief = new StudentId("12171652");
        given(repository.save(any())).willReturn(null);
        LectureRegisterForm form = LectureRegisterForm.builder()
                .title("절권도 배우기")
                .applyDeadLine(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeeks("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();

        //when
        service.create(form, chief);

        //then
        then(repository).should(times(1)).save(any());
    }

//    @DisplayName("강의실 정보 수정")
//    @Test
//    public void updateTest() {
//
//        //given
//        StudentId chief = new StudentId("12171652");
//        Lecture origin = Lecture.builder()
//                .title("절권도 배우기")
//                .chief(chief)
//                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
//                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
//                .daysOfWeek("월 금")
//                .introduction("호신술을 배워보자")
//                .method(1)
//                .participantsLimits(30)
//                .place("6호관 옥상")
//                .build();
//        ReflectionTestUtils.setField(origin, "id", 1);
//        given(repository.findById(anyInt())).willReturn(Optional.of(origin));
//        LectureUpdateForm form = LectureUpdateForm.builder()
//                .id(1)
//                .title("절권도 배우기")
//                .applyDeadLine(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
//                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
//                .daysOfWeeks("월 금")
//                .introduction("호신술을 배워보자")
//                .method(1)
//                .participantsLimits(30)
//                .place("6호관 옥상")
//                .build();
//
//        //when
//        service.update(form, new StudentId("12171652"));
//
//        //then
//        then(repository).should(times(1)).findById(any());
//    }

    @DisplayName("강의실 담당자 외에는 수정 불가")
    @Test
    public void cannotModifyTest() {

        //given
        StudentId chief = new StudentId("12171652");
        StudentId attacker = new StudentId("1");
        Lecture origin = Lecture.builder()
                .title("절권도 배우기")
                .chief(chief)
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(origin, "id", 1);
        given(repository.findById(anyInt())).willReturn(Optional.of(origin));
        LectureUpdateForm form = LectureUpdateForm.builder()
                .id(1)
                .title("절권도 배우기")
                .applyDeadLine(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeeks("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();

        //when
        Assertions.assertThrows(LectureCannotModifiableException.class,
                () -> service.update(form, attacker));

        //then
        then(repository).should(times(1)).findById(any());
    }

    @DisplayName("강의실 삭제")
    @Test
    public void deleteTest() {

        //given
        StudentId chief = new StudentId("12171652");
        Lecture origin = Lecture.builder()
                .title("절권도 배우기")
                .chief(chief)
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(origin, "id", 1);
        given(repository.findById(anyInt())).willReturn(Optional.ofNullable(origin));
        doNothing().when(repository).delete(any());

        //when
        service.delete(1, chief);

        //then
        then(repository).should(times(1)).findById(any());
        then(repository).should(times(1)).delete(any());
    }

    @DisplayName("강의실 담당자 외에는 삭제 불가")
    @Test
    public void cannotDeleteTest() {

        //given
        StudentId chief = new StudentId("12171652");
        StudentId attacker = new StudentId("1");
        Lecture origin = Lecture.builder()
                .title("절권도 배우기")
                .chief(chief)
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(origin, "id", 1);
        given(repository.findById(anyInt())).willReturn(Optional.of(origin));

        //when
        Assertions.assertThrows(LectureCannotModifiableException.class,
                () -> service.delete(1, attacker));

        //then
        then(repository).should(times(1)).findById(any());
        then(repository).should(times(0)).delete(any());
    }

    @DisplayName("강의실 정보 조회")
    @Test
    public void getTest() {

        //given
        LectureDetailDto expectedDto = LectureDetailDto.builder()
                .title("절권도 강의")
                .chief(new LectureDetailDto.MemberInfo(12171652, "산업경영공학과", "유동현"))
                .applyDeadLine(LocalDateTime.of(9022, 7, 31, 17, 11, 36))
                .created(LocalDateTime.of(9022, 7, 23, 17, 11, 36))
                .updated(null)
                .daysOfWeeks("월 금")
                .introduction("호신술을 배워보자")
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .method(1)
                .paid(false)
                .participantsLimits(30)
                .place("구내식당")
                .rejectReason(null)
                .state(LectureStatus.WAITING)
                .build()
                .setCoInstructors(List.of(
                        new LectureDetailDto.MemberInfo(12345678, "스마트모빌리티공학과", "윤예진"),
                        new LectureDetailDto.MemberInfo(87654321, "통계학과", "나까무라철수")
                ));
        given(repository.getDetails(anyInt())).willReturn(Optional.of(expectedDto));

        //when
        service.get(1);

        //then
        then(repository).should(times(1)).getDetails(any());
    }

    @DisplayName("강의실 목록 조회")
    @Test
    public void getListTest() {

        //given
        given(repository.getList(any())).willReturn(null);

        //when
        service.getList(PageRequest.of(0, 6));

        //then
        then(repository).should(times(1)).getList(any());
    }

    @DisplayName("대기 강의를 승인한다.")
    @Test
    public void approveTest() {

        //given
        Lecture origin = Lecture.builder()
                .title("절권도 배우기")
                .chief(new StudentId("12171652"))
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(origin, "id", 1);
        given(repository.findById(any())).willReturn(Optional.of(origin));

        //when
        LectureStatusUpdateRequest request = new LectureStatusUpdateRequest();
        ReflectionTestUtils.setField(request, "status", LectureStatus.PROGRESSING);
        service.approveOrDeny(1, request);

        //then
        LectureStatus status = (LectureStatus) ReflectionTestUtils.getField(request, "status");
        assertThat(status).isEqualTo(LectureStatus.PROGRESSING);
    }

    @DisplayName("대기 강의를 거절한다.")
    @Test
    public void denyTest() {
        //given
        Lecture origin = Lecture.builder()
                .title("절권도 배우기")
                .chief(new StudentId("12171652"))
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(origin, "id", 1);
        given(repository.findById(any())).willReturn(Optional.of(origin));

        //when
        LectureStatusUpdateRequest request = new LectureStatusUpdateRequest();
        ReflectionTestUtils.setField(request, "status", LectureStatus.DENIED);
        service.approveOrDeny(1, request);

        //then
        LectureStatus status = (LectureStatus) ReflectionTestUtils.getField(request, "status");
        assertThat(status).isEqualTo(LectureStatus.DENIED);
    }

    @DisplayName("강의를 강제 종료시킬 수는 없다.")
    @Test
    public void cannotTerminateLectureTest() {

        //given
        Lecture origin = Lecture.builder()
                .title("절권도 배우기")
                .chief(new StudentId("12171652"))
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(origin, "id", 1);
        given(repository.findById(any())).willReturn(Optional.of(origin));

        //when
        LectureStatusUpdateRequest request = new LectureStatusUpdateRequest();
        ReflectionTestUtils.setField(request, "status", LectureStatus.TERMINATED);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.approveOrDeny(1, request));
    }

    @DisplayName("강의를 다시 대기 상태로 되돌릴 수 없다.")
    @Test
    public void cannotMakeLectureWaitedTest() {

        //given
        Lecture origin = Lecture.builder()
                .title("절권도 배우기")
                .chief(new StudentId("12171652"))
                .applyDeadline(LocalDateTime.of(9011, 1, 1, 1, 1, 1))
                .curriculumDetails("1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...")
                .daysOfWeek("월 금")
                .introduction("호신술을 배워보자")
                .method(1)
                .participantsLimits(30)
                .place("6호관 옥상")
                .build();
        ReflectionTestUtils.setField(origin, "id", 1);
        given(repository.findById(any())).willReturn(Optional.of(origin));

        //when
        LectureStatusUpdateRequest request = new LectureStatusUpdateRequest();
        ReflectionTestUtils.setField(request, "status", LectureStatus.WAITING);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.approveOrDeny(1, request));
    }
}
