package com.inhabas.api.web;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import com.inhabas.api.domain.lecture.dto.*;
import com.inhabas.api.domain.lecture.usecase.LectureService;
import com.inhabas.api.domain.lecture.usecase.LectureStudentService;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "강의실 관리")
@RestController
@RequiredArgsConstructor
public class LectureController {

  private final LectureService lectureService;
  private final LectureStudentService studentService;

  @Operation(summary = "강의실 단건 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "400", description = "잘못된 id"),
  })
  @GetMapping("/lecture/{id}")
  public ResponseEntity<LectureDetailDto> getLectureDetails(@PathVariable Integer id) {

    return ResponseEntity.ok(lectureService.get(id));
  }

  @Operation(summary = "강의실 목록 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
  })
  @GetMapping("/lectures")
  public ResponseEntity<Page<LectureListDto>> getLectureList(
      @PageableDefault(value = 6) Pageable pageable) {

    return ResponseEntity.ok(lectureService.getList(pageable));
  }

  @Operation(summary = "강의실 등록")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
  })
  @PostMapping("/lecture")
  public ResponseEntity<?> createLecture(
      @Authenticated StudentId studentId, @Valid @RequestBody LectureRegisterForm form) {

    lectureService.create(form, studentId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "강의실 수정")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "401", description = "작성자가 아닌 경우 접근 불가"),
  })
  @PutMapping("/lecture")
  public ResponseEntity<?> updateLecture(
      @Authenticated StudentId studentId, @Valid @RequestBody LectureUpdateForm form) {

    lectureService.update(form, studentId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "강의실 삭제")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "401", description = "작성자가 아닌 경우 접근 불가"),
  })
  @DeleteMapping("/lecture/{id}")
  @PreAuthorize("@lectureSecurityChecker.instructorOnly(#id)")
  public ResponseEntity<?> deleteLecture(
      @Authenticated StudentId studentId, @PathVariable Integer id) {

    lectureService.delete(id, studentId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "강의실 상태 정보 변경 (강의 개설 승인 및 거절)", description = "종료 상태로는 변경 불가. 회장단만 변경 가능.")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "status 값은 입력 필수"),
    @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
  })
  @PutMapping("/lecture/{id}/status")
  public ResponseEntity<?> updateLectureStatus(
      @PathVariable Integer id, @Valid @RequestBody LectureStatusUpdateRequest request) {

    lectureService.approveOrDeny(id, request);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "수강생이 강의실 직접 신청하여 등록")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "잘못된 lecture_id 값"),
  })
  @PostMapping("/lecture/{id}/student")
  public ResponseEntity<?> enroll(@Authenticated StudentId studentId, @PathVariable Integer id) {

    studentService.enroll(id, studentId);

    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "수강생 한명의 수강상태를 변경",
      description = "'정상수강' or '수강정지' 만 가능 / sId 는 학번이 아니고 강의등록명단 상의 번호임을 명심할 것.")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "401", description = "강의자만 변경가능"),
  })
  @PutMapping("/lecture/{lectureId}/student/{sid}/status")
  @PreAuthorize("@lectureSecurityChecker.instructorOnly(#lectureId)")
  public ResponseEntity<?> changeStudentStatus(
      @Authenticated StudentId currentUser,
      @PathVariable Integer sid,
      @PathVariable Integer lectureId,
      @NotNull @RequestBody StudentStatus status) {

    studentService.changeStatusOfOneStudentByLecturer(sid, currentUser, status, lectureId);

    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "다수의 수강생 상태를 일괄 변경",
      description = "'정상수강' or '수강정지' 만 가능 / 단 한명이라도 오류가 발생하면 전체 rollback 됨.")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "401", description = "강의자만 변경가능")
  })
  @PutMapping("/lecture/{lectureId}/students/status")
  @PreAuthorize("@lectureSecurityChecker.instructorOnly(#lectureId)")
  public ResponseEntity<?> changeStudentsStatus(
      @Authenticated StudentId studentId,
      @PathVariable Integer lectureId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description =
                  "additionalProp 대신 studentId 값에 해당하는 정수값을 넣어야함. 단 학번이 아니라 강의등록명단 상의 번호임을 명심할 것")
          @NotNull
          @RequestBody
          Map<Integer, StudentStatus> list) {

    studentService.changeStatusOfStudentsByLecturer(list, studentId, lectureId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "수강생이 강의실을 탈퇴한다.")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
  })
  @DeleteMapping("/lecture/{id}/student")
  public ResponseEntity<?> exit(@Authenticated StudentId studentId, @PathVariable Integer id) {

    studentService.exitBySelf(id, studentId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "수강생들의 수강상태정보를 불러온다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    @ApiResponse(responseCode = "401", description = "강의자만 조회가능")
  })
  @GetMapping("/lecture/{id}/students")
  @PreAuthorize("@lectureSecurityChecker.instructorOnly(#id)")
  public ResponseEntity<Page<StudentListDto>> searchStudents(
      @PathVariable Integer id, @PageableDefault(size = 25) Pageable pageable) {

    return ResponseEntity.ok(studentService.searchStudents(id, pageable));
  }
}
