package com.inhabas.api.web;

import com.inhabas.api.domain.signUpSchedule.dto.SignUpScheduleDto;
import com.inhabas.api.domain.signUpSchedule.domain.usecase.SignUpScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name="회원가입 일정", description = "회원가입 일정 조회, 수정 / 회장만")
@RestController
@RequestMapping("/signUp/schedule")
@RequiredArgsConstructor
public class SignUpScheduleController {

    private final SignUpScheduler signUpScheduler;


    @Operation(summary = "회원가입 관련 일정을 조회한다.",
            description = "일정은 하나만 반환한다.")
    @SecurityRequirements(value = {})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = SignUpScheduleDto.class))
    })
    @GetMapping
    public ResponseEntity<SignUpScheduleDto> getSignUpSchedule() {

        return ResponseEntity.ok(signUpScheduler.getSchedule());

    }

    @Operation(summary = "회원가입 관련 일정을 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    @PutMapping
    public ResponseEntity<?> updateSignUpSchedule(@Valid @RequestBody SignUpScheduleDto signUpScheduleDto) {

        signUpScheduler.updateSchedule(signUpScheduleDto);

        return ResponseEntity.noContent().build();

    }

}
