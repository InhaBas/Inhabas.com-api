package com.inhabas.api.controller;

import com.inhabas.api.dto.member.MajorInfoDto;
import com.inhabas.api.dto.signUp.*;
import com.inhabas.api.security.argumentResolver.Authenticated;
import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.service.signup.SignUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "회원가입")
@RestController
@RequiredArgsConstructor
@RequestMapping("/signUp")
public class SignUpController {

    private final SignUpService signUpService;

    /* profile */

    @PostMapping
    @Operation(summary = "회원가입 시 개인정보를 저장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 폼 데이터")
    })
    public ResponseEntity<?> saveStudentProfile(
            @Authenticated AuthUserDetail authUser, @Valid @RequestBody SignUpDto form) {

        signUpService.saveSignUpForm(form, authUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "임시저장한 학생의 개인정보를 불러온다.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<SignUpDto> loadProfile(@Authenticated AuthUserDetail signUpUser) {

        SignUpDto form = signUpService.loadSignUpForm(signUpUser);

        return ResponseEntity.ok(form);
    }


    @GetMapping("/majorInfo")
    @Operation(summary = "회원가입에 필요한 전공 정보를 모두 불러온다.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<MajorInfoDto>> loadAllMajorInfo() {

        return ResponseEntity.ok(signUpService.getMajorInfo());
    }

    @GetMapping("/isDuplicated")
    @Operation(summary = "회원가입 시 필요한 중복검사를 진행한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "하나도 안넘기거나, 타입이 잘못된 경우")
    })
    public ResponseEntity<?> validateDuplication(
            MemberDuplicationQueryCondition condition) {

            boolean isDuplicated = signUpService.validateFieldsDuplication(condition);

            return ResponseEntity.ok(isDuplicated);
    }

    /* questionnaire */

    @GetMapping("/questionnaire")
    @Operation(summary = "회원가입에 필요한 질문들을 불러온다.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<QuestionnaireDto>> loadQuestionnaire() {

        return ResponseEntity.ok(signUpService.getQuestionnaire());
    }

    /* answer */

    @GetMapping("/answer")
    @Operation(summary = "회원가입 도중 임시 저장한 질문지 답변을 불러온다.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<AnswerDto>> loadAnswers(@Authenticated AuthUserDetail signUpUser) {

        List<AnswerDto> answers = signUpService.getAnswers(signUpUser);

        return ResponseEntity.ok(answers);
    }

    @PostMapping("/answer")
    @Operation(summary = "회원가입 시 작성한 답변을 저장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "답변이 길이제한을 초과했을 경우")
    })
    public ResponseEntity<?> saveAnswers(
            @Authenticated AuthUserDetail signUpUser, @Valid @RequestBody List<AnswerDto> answers) {

        signUpService.saveAnswers(answers, signUpUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /* finish signUp */
    @PutMapping("/finish")
    @Operation(summary = "회원가입을 완료한다")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<?> finishSignUp(@Authenticated AuthUserDetail signUpUser) {

        signUpService.completeSignUp(signUpUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
