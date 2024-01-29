package com.inhabas.api.web;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.signUpSchedule.dto.SignUpScheduleDto;
import com.inhabas.api.domain.signUpSchedule.usecase.SignUpScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원가입 일정", description = "회원가입 일정 조회, 수정 / 회장만")
@RestController
@RequestMapping("/signUp/schedule")
@RequiredArgsConstructor
public class SignUpScheduleController {

  private final SignUpScheduler signUpScheduler;

  @Operation(summary = "회원가입 관련 일정을 조회한다.", description = "일정은 하나만 반환한다.")
  @SecurityRequirements(value = {})
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = SignUpScheduleDto.class))})
  @GetMapping
  public ResponseEntity<SignUpScheduleDto> getSignUpSchedule() {

    return ResponseEntity.ok(signUpScheduler.getSchedule());
  }

  @Operation(summary = "회원가입 관련 일정을 수정한다.")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(
        responseCode = "400",
        description = "등록 마감일이 시작일보다 앞설 수 없습니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                  @ExampleObject(
                      name = "등록 마감일 오류",
                      value =
                          "{\"status\": 400, \"code\": \"S002\", \"message\": \"등록 마감일이 시작일보다 앞설 수 없습니다.\"}"),
                  @ExampleObject(
                      name = "면접 마감일 오류",
                      value =
                          "{\"status\": 400, \"code\": \"S003\", \"message\": \"면접 마감일이 시작일보다 앞설 수 없습니다.\"}"),
                  @ExampleObject(
                      name = "결과 발표일 오류",
                      value =
                          "{\"status\": 400, \"code\": \"S004\", \"message\": \"결과 발표일이 면접 마감일보다 앞설 수 없습니다.\"}")
                }))
  })
  @PutMapping
  public ResponseEntity<?> updateSignUpSchedule(
      @Valid @RequestBody SignUpScheduleDto signUpScheduleDto) {

    signUpScheduler.updateSchedule(signUpScheduleDto);

    return ResponseEntity.noContent().build();
  }
}
