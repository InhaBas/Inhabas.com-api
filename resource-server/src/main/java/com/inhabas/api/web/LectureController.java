package com.inhabas.api.web;

import com.inhabas.api.domain.lecture.dto.*;
import com.inhabas.api.domain.lecture.usecase.LectureService;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "강의실 관리")
@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

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
            @Authenticated MemberId memberId, @Valid @RequestBody LectureRegisterForm form) {

        lectureService.create(form, memberId);

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
            @Authenticated MemberId memberId, @Valid @RequestBody LectureUpdateForm form) {

        lectureService.update(form, memberId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "강의실 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "작성자가 아닌 경우 접근 불가"),
    })
    @DeleteMapping("/lecture/{id}")
    public ResponseEntity<?> deleteLecture(
            @Authenticated MemberId memberId, @PathVariable Integer id) {

        lectureService.delete(id, memberId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "강의실 상태 정보 변경 (강의 개설 승인 및 거절)",
            description = "종료 상태로는 변경 불가. 회장단만 변경 가능.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "status 값은 입력 필수"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    @PutMapping("/lecture/{id}/status")
    public ResponseEntity<?> updateLectureStatus(@PathVariable Integer id, @Valid @RequestBody StatusUpdateRequest request) {

        lectureService.approveOrDeny(id, request);

        return ResponseEntity.noContent().build();
    }
}
