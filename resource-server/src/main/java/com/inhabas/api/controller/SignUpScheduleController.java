package com.inhabas.api.controller;

import com.inhabas.api.dto.signUp.SignUpScheduleDto;
import com.inhabas.api.service.signup.SignUpScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name="회원가입 일정")
@RestController
@RequestMapping("/signUp/schedule")
@RequiredArgsConstructor
public class SignUpScheduleController {

    private final SignUpScheduler signUpScheduler;

    @GetMapping
    @Operation(summary = "회원가입 관련 일정을 조회한다.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<SignUpScheduleDto> getSignUpSchedule() {

        return ResponseEntity.ok(signUpScheduler.getSchedule());
    }

    @PutMapping
    @Operation(summary = "회원가입 관련 일정을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "401", description = "회장만 변경 가능하다.")
    })
    public ResponseEntity<?> updateSignUpSchedule(@Valid @RequestBody SignUpScheduleDto signUpScheduleDto) {

        signUpScheduler.updateSchedule(signUpScheduleDto);

        return ResponseEntity.noContent().build();
    }

}
