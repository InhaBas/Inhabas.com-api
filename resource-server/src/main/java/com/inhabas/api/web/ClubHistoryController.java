package com.inhabas.api.web;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import com.inhabas.api.domain.club.usecase.ClubHistoryService;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "동아리 소개", description = "동아리 소개 관련")
@RestController
@RequiredArgsConstructor
public class ClubHistoryController {

  private final ClubHistoryService clubHistoryService;

  @Operation(summary = "동아리 연혁 조회", description = "동아리 연혁 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {
              @Content(schema = @Schema(implementation = ClubHistoryDto.class, type = "array"))
            }),
      })
  @SecurityRequirements(value = {})
  @GetMapping("/club/histories")
  public ResponseEntity<List<ClubHistoryDto>> getClubHistories() {

    List<ClubHistoryDto> clubHistoryDtoList = clubHistoryService.getClubHistories();
    return ResponseEntity.ok(clubHistoryDtoList);
  }

  @Operation(summary = "동아리 연혁 단일 조회", description = "동아리 연혁 단일 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = ClubHistoryDto.class))}),
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
  @SecurityRequirements(value = {})
  @GetMapping("/club/history/{clubHistoryId}")
  public ResponseEntity<ClubHistoryDto> findClubHistory(@PathVariable Long clubHistoryId) {

    ClubHistoryDto clubHistoryDto = clubHistoryService.findClubHistory(clubHistoryId);
    return ResponseEntity.ok(clubHistoryDto);
  }

  @Operation(summary = "동아리 연혁 생성", description = "동아리 연혁 생성")
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
  @PutMapping("/club/history/{clubHistoryId}")
  public ResponseEntity<Void> updateClubHistory(
      @PathVariable Long clubHistoryId,
      @Authenticated Long memberId,
      @Valid @RequestBody SaveClubHistoryDto saveClubHistoryDto) {

    clubHistoryService.updateClubHistory(memberId, clubHistoryId, saveClubHistoryDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "동아리 연혁 삭제", description = "동아리 연혁 삭제")
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
  @DeleteMapping("/club/history/{clubHistoryId}")
  public ResponseEntity<Void> deleteClubHistory(@PathVariable Long clubHistoryId) {

    clubHistoryService.deleteClubHistory(clubHistoryId);
    return ResponseEntity.noContent().build();
  }
}
