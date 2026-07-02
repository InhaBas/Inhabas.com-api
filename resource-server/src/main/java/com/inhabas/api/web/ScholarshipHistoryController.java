package com.inhabas.api.web;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;
import com.inhabas.api.domain.scholarship.usecase.ScholarshipHistoryService;
import com.inhabas.api.global.swagger.Response201And400;
import com.inhabas.api.global.swagger.Response204And400And404;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "장학회 연혁", description = "장학회 소개 관련")
@RestController
@RequiredArgsConstructor
public class ScholarshipHistoryController {

  private final ScholarshipHistoryService scholarshipHistoryService;

  @Operation(summary = "장학회 연혁 조회")
  @SecurityRequirements(value = {})
  @GetMapping("/scholarship/histories")
  public ResponseEntity<List<YearlyData>> getScholarHistories() {

    List<YearlyData> dtoList = scholarshipHistoryService.getScholarshipHistories();
    return ResponseEntity.ok(dtoList);
  }

  @Operation(summary = "장학회 연혁 생성")
  @Response201And400
  @PostMapping("/scholarship/history")
  public ResponseEntity<Void> writeScholarshipHistory(
      @Authenticated Long memberId,
      @Valid @RequestBody SaveScholarshipHistoryDto saveScholarshipHistoryDto) {

    Long newScholarshipHistoryId =
        scholarshipHistoryService.writeScholarshipHistory(memberId, saveScholarshipHistoryDto);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{scholarshipHistoryId}")
            .buildAndExpand(newScholarshipHistoryId)
            .toUri();
    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "장학회 연혁 수정")
  @Response204And400And404
  @PutMapping("/scholarship/history/{scholarshipHistoryId}")
  public ResponseEntity<Void> updateScholarshipHistory(
      @PathVariable Long scholarshipHistoryId,
      @Authenticated Long memberId,
      @Valid @RequestBody SaveScholarshipHistoryDto saveScholarshipHistoryDto) {

    scholarshipHistoryService.updateScholarshipHistory(
        memberId, scholarshipHistoryId, saveScholarshipHistoryDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "장학회 연혁 삭제", description = "장학회 연혁 삭제")
  @Response204And400And404
  @DeleteMapping("/scholarship/history/{scholarshipHistoryId}")
  public ResponseEntity<Void> deleteScholarshipHistory(@PathVariable Long scholarshipHistoryId) {

    scholarshipHistoryService.deleteScholarshipHistory(scholarshipHistoryId);
    return ResponseEntity.noContent().build();
  }
}
