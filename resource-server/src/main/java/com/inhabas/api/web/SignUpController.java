package com.inhabas.api.web;

import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.dto.SignUpDto;
import com.inhabas.api.domain.signUp.usecase.SignUpService;
import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "회원가입", description = "회원가입 기간이 아니면 403 Forbidden")
@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;


    /* profile */

    @GetMapping("/signUp")
    @Operation(summary = "자신이 임시저장한 개인정보를 불러온다.",
    description = "저장한 이력이 없다면 모두 null 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = SignUpDto.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    public ResponseEntity<SignUpDto> loadProfile(
            @Authenticated Long memberId) {

        SignUpDto form = signUpService.loadSignUpForm(memberId);

        return ResponseEntity.ok(form);

    }

    @Operation(summary = "회원가입 시 자신의 개인정보를 저장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다."),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    @PostMapping("/signUp")
    public ResponseEntity<?> saveStudentProfile(
            @Authenticated Long memberId, @Valid @RequestBody SignUpDto form) {

        signUpService.saveSignUpForm(form, memberId);

        return ResponseEntity.noContent().build();

    }


    @Operation(summary = "회원가입에 필요한 전공 정보를 모두 불러온다. (권한 필요 X)")
    @ApiResponse(responseCode = "200", content = @Content(
            schema = @Schema(implementation = SignUpDto.class)))
    @GetMapping("/signUp/majorInfo")
    public ResponseEntity<List<MajorInfoDto>> loadAllMajorInfo() {

        return ResponseEntity.ok(signUpService.getMajorInfo());

    }


    /* questionnaire */

    @Operation(summary = "회원가입에 필요한 질문들을 불러온다. (권한 필요 X)")
    @ApiResponse(responseCode = "200", content = @Content(
            schema = @Schema(implementation = QuestionnaireDto.class)))
    @GetMapping("/signUp/questionnaires")
    public ResponseEntity<List<QuestionnaireDto>> loadQuestionnaire() {

        return ResponseEntity.ok(signUpService.getQuestionnaire());

    }

    /* answer */

    @GetMapping("/signUp/answers")
    @Operation(summary = "회원가입 도중 자신이 임시 저장한 질문지 답변을 불러온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = AnswerDto.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    public ResponseEntity<List<AnswerDto>> loadAnswers(@Authenticated Long memberId) {

        List<AnswerDto> answers = signUpService.getAnswers(memberId);

        return ResponseEntity.ok(answers);
    }

    @PostMapping("/signUp/answers")
    @Operation(summary = "회원가입 시 자신이 작성한 답변을 임시 저장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "답변이 길이제한을 초과했을 경우"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    public ResponseEntity<?> saveAnswers(
            @Authenticated Long memberId, @Valid @RequestBody List<AnswerDto> answers) {

        signUpService.saveAnswers(answers, memberId);

        return ResponseEntity.noContent().build();

    }

    /* finish signUp */
    @PutMapping("/signUp")
    @Operation(summary = "회원가입을 완료한다")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<?> finishSignUp(@Authenticated Long memberId, @Valid @RequestBody List<AnswerDto> answers) {

        signUpService.completeSignUp(answers, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
