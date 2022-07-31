package com.inhabas.api.web;

import com.inhabas.api.domain.lecture.dto.LectureDetailDto;
import com.inhabas.api.domain.lecture.dto.LectureListDto;
import com.inhabas.api.domain.lecture.dto.LectureRegisterForm;
import com.inhabas.api.domain.lecture.dto.LectureUpdateForm;
import com.inhabas.api.domain.lecture.usecase.LectureService;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping("/lecture/{id}")
    public ResponseEntity<LectureDetailDto> getLectureDetails(@PathVariable Integer id) {

        return ResponseEntity.ok(lectureService.get(id));
    }

    @Operation(summary = "강의실 목록 조회")
    @GetMapping("/lectures")
    public ResponseEntity<Page<LectureListDto>> getLectureList(
            @PageableDefault(value = 6) Pageable pageable) {

        return ResponseEntity.ok(lectureService.getList(pageable));
    }

    @Operation(summary = "강의실 등록")
    @PostMapping("/lecture")
    public ResponseEntity<?> createLecture(
            @Authenticated MemberId memberId, @Valid @RequestBody LectureRegisterForm form) {

        lectureService.create(form, memberId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "강의실 수정")
    @PutMapping("/lecture")
    public ResponseEntity<?> updateLecture(
            @Authenticated MemberId memberId, @Valid @RequestBody LectureUpdateForm form) {

        lectureService.update(form, memberId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "강의실 삭제")
    @DeleteMapping("/lecture/{id}")
    public ResponseEntity<?> deleteLecture(
            @Authenticated MemberId memberId, @PathVariable Integer id) {

        lectureService.delete(id, memberId);

        return ResponseEntity.noContent().build();
    }
}
