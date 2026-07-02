package com.inhabas.api.web;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import com.inhabas.api.domain.club.usecase.ClubHistoryService;
import com.inhabas.api.global.swagger.Response200And400And404;
import com.inhabas.api.global.swagger.Response201And400;
import com.inhabas.api.global.swagger.Response204And400And404;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "동아리 소개", description = "동아리 소개 관련")
@RestController
@RequiredArgsConstructor
public class ClubHistoryController {

  private final ClubHistoryService clubHistoryService;

  @Operation(summary = "동아리 연혁 조회", description = "동아리 연혁 조회")
  @SecurityRequirements(value = {})
  @GetMapping("/club/histories")
  public ResponseEntity<List<ClubHistoryDto>> getClubHistories() {

    List<ClubHistoryDto> clubHistoryDtoList = clubHistoryService.getClubHistories();
    return ResponseEntity.ok(clubHistoryDtoList);
  }

  @Operation(summary = "동아리 연혁 단일 조회", description = "동아리 연혁 단일 조회")
  @Response200And400And404
  @SecurityRequirements(value = {})
  @GetMapping("/club/history/{clubHistoryId}")
  public ResponseEntity<ClubHistoryDto> findClubHistory(@PathVariable Long clubHistoryId) {

    ClubHistoryDto clubHistoryDto = clubHistoryService.findClubHistory(clubHistoryId);
    return ResponseEntity.ok(clubHistoryDto);
  }

  @Operation(summary = "동아리 연혁 생성", description = "동아리 연혁 생성")
  @Response201And400
  @PostMapping("/club/history")
  public ResponseEntity<Void> writeClubHistory(
      @Authenticated Long memberId, @Valid @RequestBody SaveClubHistoryDto saveClubHistoryDto) {

    Long newClubHistoryId = clubHistoryService.writeClubHistory(memberId, saveClubHistoryDto);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{clubHistoryId}")
            .buildAndExpand(newClubHistoryId)
            .toUri();
    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "동아리 연혁 수정", description = "동아리 연혁 수정")
  @Response204And400And404
  @PutMapping("/club/history/{clubHistoryId}")
  public ResponseEntity<Void> updateClubHistory(
      @PathVariable Long clubHistoryId,
      @Authenticated Long memberId,
      @Valid @RequestBody SaveClubHistoryDto saveClubHistoryDto) {

    clubHistoryService.updateClubHistory(memberId, clubHistoryId, saveClubHistoryDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "동아리 연혁 삭제", description = "동아리 연혁 삭제")
  @Response204And400And404
  @DeleteMapping("/club/history/{clubHistoryId}")
  public ResponseEntity<Void> deleteClubHistory(@PathVariable Long clubHistoryId) {

    clubHistoryService.deleteClubHistory(clubHistoryId);
    return ResponseEntity.noContent().build();
  }
}
