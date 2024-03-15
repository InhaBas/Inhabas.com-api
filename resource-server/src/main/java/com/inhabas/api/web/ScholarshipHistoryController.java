package com.inhabas.api.web;

import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

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

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;
import com.inhabas.api.domain.scholarship.usecase.ScholarshipHistoryService;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "장학회 연혁", description = "장학회 소개 관련")
@RestController
@RequiredArgsConstructor
public class ScholarshipHistoryController {

  private final ScholarshipHistoryService scholarshipHistoryService;

  @Operation(summary = "장학회 연혁 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {
              @Content(
                  schema = @Schema(implementation = YearlyData.class, type = "array"))
            }),
      })
  @SecurityRequirements(value = {})
  @GetMapping("/scholarship/histories")
  public ResponseEntity<List<YearlyData>> getScholarHistories() {

    List<YearlyData> dtoList = scholarshipHistoryService.getScholarshipHistories();
    return ResponseEntity.ok(dtoList);
  }

  @Operation(summary = "장학회 연혁 생성")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "'Location' 헤더에 생성된 리소스의 URI 가 포함됩니다."),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}")))
      })
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
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "데이터가 존재하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
      })
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
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "데이터가 존재하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
      })
  @DeleteMapping("/scholarship/history/{scholarshipHistoryId}")
  public ResponseEntity<Void> deleteScholarshipHistory(@PathVariable Long scholarshipHistoryId) {

    scholarshipHistoryService.deleteScholarshipHistory(scholarshipHistoryId);
    return ResponseEntity.noContent().build();
  }
}
