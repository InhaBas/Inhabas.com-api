package com.inhabas.api.web;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;
import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.dto.SignUpDto;
import com.inhabas.api.domain.signUp.usecase.SignUpService;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원가입", description = "회원가입 기간이 아니면 403 Forbidden")
@RestController
@RequiredArgsConstructor
public class SignUpController {

  private final SignUpService signUpService;

  @GetMapping("/signUp/check")
  @Operation(summary = "요청을 보낸 사용자가 회원가입을 했는지 확인한다.", description = "회원가입을 이미 했다면 true, 아니면 false")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = @Content(schema = @Schema(implementation = SignUpDto.class))),
    @ApiResponse(
        responseCode = "403 ",
        description = "회원가입 기간이 아닙니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}")))
  })
  public ResponseEntity<Map<String, Boolean>> signUpCheck(@Authenticated Long memberId) {
    boolean check = signUpService.isSignedUp(memberId);
    return ResponseEntity.ok(Collections.singletonMap("check", check));
  }

  @Operation(summary = "회원가입 시 자신의 개인정보를 저장한다.")
  @ApiResponses({
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
        responseCode = "403 ",
        description = "회원가입 기간이 아닙니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}")))
  })
  @PostMapping("/signUp")
  public ResponseEntity<?> saveStudentProfile(
      @Authenticated Long memberId, @Valid @RequestBody SignUpDto form) {

    signUpService.saveSignUpForm(form, memberId);

    return ResponseEntity.noContent().build();
  }

  /* profile */

  @GetMapping("/signUp")
  @Operation(summary = "자신이 임시저장한 개인정보를 불러온다.", description = "저장한 이력이 없다면 모두 null 반환")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = @Content(schema = @Schema(implementation = SignUpDto.class))),
    @ApiResponse(
        responseCode = "403 ",
        description = "회원가입 기간이 아닙니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}")))
  })
  public ResponseEntity<SignUpDto> loadProfile(@Authenticated Long memberId) {

    SignUpDto form = signUpService.loadSignUpForm(memberId);

    return ResponseEntity.ok(form);
  }

  @Operation(summary = "회원가입에 필요한 전공 정보를 모두 불러온다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = @Content(schema = @Schema(implementation = SignUpDto.class))),
    @ApiResponse(
        responseCode = "403 ",
        description = "회원가입 기간이 아닙니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}")))
  })
  @SecurityRequirements(value = {})
  @GetMapping("/signUp/majorInfo")
  public ResponseEntity<List<MajorInfoDto>> loadAllMajorInfo() {

    return ResponseEntity.ok(signUpService.getMajorInfo());
  }

  /* questionnaire */

  @Operation(summary = "회원가입에 필요한 질문들을 불러온다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = @Content(schema = @Schema(implementation = QuestionnaireDto.class))),
    @ApiResponse(
        responseCode = "403 ",
        description = "회원가입 기간이 아닙니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}")))
  })
  @SecurityRequirements(value = {})
  @GetMapping("/signUp/questionnaires")
  public ResponseEntity<List<QuestionnaireDto>> loadQuestionnaire() {

    return ResponseEntity.ok(signUpService.getQuestionnaire());
  }

  /* answer */

  @GetMapping("/signUp/answers")
  @Operation(summary = "회원가입 도중 자신이 임시 저장한 질문지 답변을 불러온다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = @Content(schema = @Schema(implementation = AnswerDto.class))),
    @ApiResponse(
        responseCode = "403 ",
        description = "회원가입 기간이 아닙니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}")))
  })
  public ResponseEntity<List<AnswerDto>> loadAnswers(@Authenticated Long memberId) {

    List<AnswerDto> answers = signUpService.getAnswers(memberId);

    return ResponseEntity.ok(answers);
  }

  @PostMapping("/signUp/answers")
  @Operation(summary = "회원가입 시 자신이 작성한 답변을 임시 저장한다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
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
        responseCode = "403 ",
        description = "회원가입 기간이 아닙니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}")))
  })
  public ResponseEntity<?> saveAnswers(
      @Authenticated Long memberId, @Valid @RequestBody List<AnswerDto> answers) {

    signUpService.saveAnswers(answers, memberId);

    return ResponseEntity.ok().build();
  }

  /* finish signUp */
  @PutMapping("/signUp")
  @Operation(summary = "회원가입을 완료한다")
  @ApiResponses({
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
        responseCode = "403 ",
        description = "회원가입 기간이 아닙니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}")))
  })
  public ResponseEntity<?> finishSignUp(
      @Authenticated Long memberId, @Valid @RequestBody Optional<List<AnswerDto>> answers) {

    signUpService.completeSignUp(answers.orElse(new ArrayList<>()), memberId);
    return ResponseEntity.noContent().build();
  }
}
