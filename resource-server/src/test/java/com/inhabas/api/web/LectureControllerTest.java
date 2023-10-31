package com.inhabas.api.web;

import com.inhabas.api.domain.lecture.domain.valueObject.LectureStatus;
import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import com.inhabas.api.domain.lecture.dto.LectureDetailDto;
import com.inhabas.api.domain.lecture.dto.LectureListDto;
import com.inhabas.api.domain.lecture.dto.StudentListDto;
import com.inhabas.api.domain.lecture.usecase.LectureService;
import com.inhabas.api.domain.lecture.usecase.LectureStudentService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoSecureWebMvcTest(LectureController.class)
public class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LectureService lectureService;

    @MockBean
    private LectureStudentService studentService;

    @DisplayName("강의 등록 uri 를 정상적으로 호출 및 응답.")
    @Test
    public void createLectureTest() throws Exception {

        //given
        doNothing().when(lectureService).create(any(), any());

        //when
        mockMvc.perform(post("/lecture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"title\": \"절권도 강의\",\n" +
                                "  \"applyDeadLine\": \"9022-07-31T16:51:33\",\n" +
                                "  \"daysOfWeeks\": \"월 수 금\",\n" +
                                "  \"place\": \"6호관 623 강의실\",\n" +
                                "  \"curriculumDetails\": \"1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...\",\n" +
                                "  \"participantsLimits\": 30,\n" +
                                "  \"method\": 1,\n" +
                                "  \"introduction\": \"호신술을 배워보자\"\n" +
                                "}"))
                .andExpect(status().isNoContent());

        //then
        then(lectureService).should(times(1)).create(any(), any());
    }

    @DisplayName("강의 수정 uri 를 정상적으로 호출 및 응답")
    @Test
    public void updateLectureTest() throws Exception {

        //given
        doNothing().when(lectureService).update(any(), any());

        //when
        mockMvc.perform(put("/lecture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": \"1\",\n" +
                                "  \"title\": \"절권도 강의\",\n" +
                                "  \"applyDeadLine\": \"9022-07-31T16:51:33\",\n" +
                                "  \"daysOfWeeks\": \"월 수 금\",\n" +
                                "  \"place\": \"6호관 623 강의실\",\n" +
                                "  \"curriculumDetails\": \"1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...\",\n" +
                                "  \"participantsLimits\": 30,\n" +
                                "  \"method\": 1,\n" +
                                "  \"introduction\": \"호신술을 배워보자\"\n" +
                                "}"))
                .andExpect(status().isNoContent());

        //then
        then(lectureService).should(times(1)).update(any(), any());
    }

    @DisplayName("강의 삭제 uri 를 정상적으로 호출 및 응답")
    @Test
    public void deleteLectureTest() throws Exception {

        //given
        doNothing().when(lectureService).delete(any(), any());

        //when
        mockMvc.perform(delete("/lecture/1"))
                .andExpect(status().isNoContent());

        //then
        then(lectureService).should(times(1)).delete(any(), any());
    }

    @DisplayName("강의 조회 uri 를 정상적으로 호출 및 응답")
    @Test
    public void getLectureDetailsTest() throws Exception {

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
        given(lectureService.get(any())).willReturn(expectedDto);

        //when
        String response = mockMvc.perform(get("/lecture/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        //then
        then(lectureService).should(times(1)).get(any());
        JSONAssert.assertEquals("{" +
                "  \"title\": \"절권도 강의\"," +
                "  \"chief\": {" +
                "    \"id\": 12171652," +
                "    \"major\": \"산업경영공학과\"," +
                "    \"name\": \"유동현\"" +
                "  }," +
                "  \"coInstructors\": [" +
                "    {" +
                "      \"id\": 12345678," +
                "      \"major\": \"스마트모빌리티공학과\"," +
                "      \"name\": \"윤예진\"" +
                "    }," +
                "    {" +
                "      \"id\": 87654321," +
                "      \"major\": \"통계학과\"," +
                "      \"name\": \"나까무라철수\"" +
                "    }" +
                "  ]," +
                "  \"applyDeadLine\": \"9022-07-31T17:11:36\"," +
                "  \"daysOfWeeks\": \"월 금\"," +
                "  \"place\": \"구내식당\"," +
                "  \"introduction\": \"호신술을 배워보자\"," +
                "  \"curriculumDetails\": \"1주차: 빅데이터에 기반한 공격패턴분석<br> 2주차: ...\"," +
                "  \"participantsLimits\": 30," +
                "  \"method\": 1," +
                "  \"state\": \"WAITING\"," +
                "  \"rejectReason\": null," +
                "  \"paid\": false," +
                "  \"created\": \"9022-07-23T17:11:36\"," +
                "  \"updated\": null" +
                "}", response, false);
    }

    @DisplayName("강의 리스트 조회 uri 를 정상적으로 호출 및 응답")
    @Test
    public void getLectureListTest() throws Exception {

        //given
        List<LectureListDto> list = List.of(
                new LectureListDto(1, "title1", 1000001, "intro1", LocalDateTime.of(2001, 1, 1, 1, 1, 1), LectureStatus.PROGRESSING, 1, 30, 2),
                new LectureListDto(2, "title2", 1000002, "intro2", LocalDateTime.of(2002, 2, 2, 2, 2, 2), LectureStatus.PROGRESSING, 1, 31, 3),
                new LectureListDto(3, "title3", 1000003, "intro3", LocalDateTime.of(2003, 3, 3, 3, 3, 3), LectureStatus.PROGRESSING, 1, 32, 4)
        );
        PageImpl<LectureListDto> page = new PageImpl<>(list, PageRequest.of(0, 6, Sort.Direction.DESC, "apply_deadline"), list.size());
        given(lectureService.getList(any())).willReturn(page);

        //when
        String response = mockMvc.perform(get("/lectures"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        //then
        then(lectureService).should(times(1)).getList(any());
        JSONAssert.assertEquals("{\n" +
                "  \"totalPages\": 1,\n" +
                "  \"totalElements\": 3,\n" +
                "  \"size\": 6,\n" +
                "  \"content\": [\n" +
                "    {\n" +
                "      \"lectureId\": 3,\n" +
                "      \"title\": \"title3\",\n" +
                "      \"chiefId\": 1000003,\n" +
                "      \"introduction\": \"intro3\",\n" +
                "      \"applyDeadline\": \"2003-03-03T03:03:03\",\n" +
                "      \"status\": \"PROGRESSING\",\n" +
                "      \"method\": 1,\n" +
                "      \"participantsLimits\": 32,\n" +
                "      \"theNumberOfCurrentParticipants\": 4\n" +
                "    },\n" +
                "    {\n" +
                "      \"lectureId\": 2,\n" +
                "      \"title\": \"title2\",\n" +
                "      \"chiefId\": 1000002,\n" +
                "      \"introduction\": \"intro2\",\n" +
                "      \"applyDeadline\": \"2002-02-02T02:02:02\",\n" +
                "      \"status\": \"PROGRESSING\",\n" +
                "      \"method\": 1,\n" +
                "      \"participantsLimits\": 31,\n" +
                "      \"theNumberOfCurrentParticipants\": 3\n" +
                "    },\n" +
                "    {\n" +
                "      \"lectureId\": 1,\n" +
                "      \"title\": \"title1\",\n" +
                "      \"chiefId\": 1000001,\n" +
                "      \"introduction\": \"intro1\",\n" +
                "      \"applyDeadline\": \"2001-01-01T01:01:01\",\n" +
                "      \"status\": \"PROGRESSING\",\n" +
                "      \"method\": 1,\n" +
                "      \"participantsLimits\": 30,\n" +
                "      \"theNumberOfCurrentParticipants\": 2\n" +
                "    }\n" +
                "  ],\n" +
                "  \"number\": 0,\n" +
                "  \"sort\": {\n" +
                "    \"empty\": false,\n" +
                "    \"sorted\": true,\n" +
                "    \"unsorted\": false\n" +
                "  },\n" +
                "  \"first\": true,\n" +
                "  \"last\": true,\n" +
                "  \"numberOfElements\": 3,\n" +
                "  \"pageable\": {\n" +
                "    \"offset\": 0,\n" +
                "    \"sort\": {\n" +
                "      \"empty\": false,\n" +
                "      \"sorted\": true,\n" +
                "      \"unsorted\": false\n" +
                "    },\n" +
                "    \"pageNumber\": 0,\n" +
                "    \"pageSize\": 6,\n" +
                "    \"paged\": true,\n" +
                "    \"unpaged\": false\n" +
                "  },\n" +
                "  \"empty\": false\n" +
                "}", response, false);
    }

    @DisplayName("강의 상태 변경 api 를 호출한다.")
    @Test
    public void updateLectureStatusTest() throws Exception {

        //given
        doNothing().when(lectureService).approveOrDeny(any(), any());

        //when
        mockMvc.perform(put("/lecture/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"status\": \"PROGRESSING\",\n" +
                                "  \"rejectReason\": null\n" +
                                "}"))
                .andExpect(status().isNoContent());

        //then
        then(lectureService).should(times(1)).approveOrDeny(any(), any());
    }

    @DisplayName("수강생으로 등록한다.")
    @Test
    public void enrollTest() throws Exception {

        //given
        doNothing().when(studentService).enroll(any(), any());

        //when
        mockMvc.perform(post("/lecture/1/student"))
                .andExpect(status().isNoContent());

        //then
        then(studentService).should(times(1)).enroll(any(), any());
    }

    @DisplayName("수강생 한명의 수강상태를 변경한다.")
    @Test
    public void changeStudentStatusTest() throws Exception {

        doNothing().when(studentService).changeStatusOfOneStudentByLecturer(any(), any(), any(), any());

        //when
        mockMvc.perform(put("/lecture/1/student/121/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"BLOCKED\""))
                .andExpect(status().isNoContent());

        //then
        then(studentService).should(times(1)).changeStatusOfOneStudentByLecturer(any(), any(), any(), any());
    }

    @DisplayName("다수의 수강생 상태를 변경한다.")
    @Test
    public void changeStudentsStatusTest() throws Exception {

        doNothing().when(studentService).changeStatusOfStudentsByLecturer(any(), any(), any());

        //when
        mockMvc.perform(put("/lecture/1/students/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"12\":\"BLOCKED\"}, {\"15\":\"BLOCKED\"}"))
                .andExpect(status().isNoContent());

        //then
        then(studentService).should(times(1)).changeStatusOfStudentsByLecturer(any(), any(), any());
    }

    @DisplayName("수강생이 강의를 탈퇴한다.")
    @Test
    public void exitLectureTest() throws Exception {

        doNothing().when(studentService).exitBySelf(any(), any());

        //when
        mockMvc.perform(delete("/lecture/1/student"))
                .andExpect(status().isNoContent());

        //then
        then(studentService).should(times(1)).exitBySelf(any(), any());
    }

    @DisplayName("수강생들의 정보를 조회한다.")
    @Test
    public void searchStudentsInformation() throws Exception {

        //given
        List<StudentListDto> students = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            students.add(
                    StudentListDto.builder()
                            .name("홍길동"+i)
                            .memberId(1000000+i)
                            .email("my@gmail.com")
                            .assignmentCount(0)
                            .attendanceCount(0)
                            .phone("010-0000-0000")
                            .status(StudentStatus.PROGRESS)
                            .sid(i)
                            .build()
            );
        }
        PageImpl<StudentListDto> page =
                new PageImpl<>(students.subList(50, students.size()), PageRequest.of(2, 25, Sort.Direction.ASC, "memberId"), students.size());
        given(studentService.searchStudents(any(), any())).willReturn(page);

        //when
        String response = mockMvc.perform(get("/lecture/1/students"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        //then
        then(studentService).should(times(1)).searchStudents(any(), any());
        JSONAssert.assertEquals("{\n" +
                "  \"totalPages\": 3,\n" +
                "  \"totalElements\": 51,\n" +
                "  \"size\": 25,\n" +
                "  \"content\": [\n" +
                "    {\n" +
                "      \"name\": \"홍길동50\",\n" +
                "      \"memberId\": 1000050,\n" +
                "      \"phone\": \"010-0000-0000\",\n" +
                "      \"email\": \"my@gmail.com\",\n" +
                "      \"assignmentCount\": 0,\n" +
                "      \"attendanceCount\": 0,\n" +
                "      \"status\": \"PROGRESS\",\n" +
                "      \"sid\": 50\n" +
                "    }\n" +
                "  ],\n" +
                "  \"number\": 2,\n" +
                "  \"sort\": {\n" +
                "    \"empty\": false,\n" +
                "    \"sorted\": true,\n" +
                "    \"unsorted\": false\n" +
                "  },\n" +
                "  \"first\": false,\n" +
                "  \"last\": true,\n" +
                "  \"numberOfElements\": 1,\n" +
                "  \"pageable\": {\n" +
                "    \"offset\": 50,\n" +
                "    \"sort\": {\n" +
                "      \"empty\": false,\n" +
                "      \"sorted\": true,\n" +
                "      \"unsorted\": false\n" +
                "    },\n" +
                "    \"pageNumber\": 2,\n" +
                "    \"pageSize\": 25,\n" +
                "    \"paged\": true,\n" +
                "    \"unpaged\": false\n" +
                "  },\n" +
                "  \"empty\": false\n" +
                "}", response, false);
    }
}
