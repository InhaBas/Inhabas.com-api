package com.inhabas.api.controller;

import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.DetailSignUpDto;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import com.inhabas.api.dto.signUp.StudentSignUpDto;
import com.inhabas.api.security.argumentResolver.AuthenticatedAuthUser;
import com.inhabas.api.security.domain.AuthUser;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.api.service.questionnaire.AnswerService;
import com.inhabas.api.service.questionnaire.QuestionnaireService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final MemberService memberService;
    private final AnswerService answerService;
    private final QuestionnaireService questionnaireService;

    /* profile */

    @PostMapping("/signUp/student")
    @Operation(description = "회원가입 시 프로필을 저장한다.")
    public ResponseEntity<?> saveProfile(@AuthenticatedAuthUser AuthUser authUser, @Valid @RequestBody StudentSignUpDto form) {
        memberService.signUp(authUser, form);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/signUp/student")
    @Operation(description = "임시저장한 개인정보를 불러온다.")
    public ResponseEntity<DetailSignUpDto> loadProfile(@AuthenticatedAuthUser AuthUser signUpUser) {
        DetailSignUpDto form = memberService.loadSignUpForm(signUpUser.getProfile().getId(), signUpUser.getEmail());

        return ResponseEntity.ok(form);
    }


    /* questionnaire */

    @GetMapping("/signUp/questionnaire")
    @Operation(description = "회원가입에 필요한 질문들을 불러온다.")
    public ResponseEntity<List<QuestionnaireDto>> loadQuestionnaire() {

        return ResponseEntity.ok(questionnaireService.getQuestionnaire());
    }

    /* answer */

    @GetMapping("/signUp/answer")
    @Operation(description = "회원가입 도중 임시 저장한 질문지 답변을 불러온다.")
    public ResponseEntity<List<AnswerDto>> loadAnswers(@AuthenticatedAuthUser AuthUser signUpUser) {
        List<AnswerDto> answers = answerService.getAnswers(signUpUser.getProfile().getId());

        return ResponseEntity.ok(answers);
    }

    @PostMapping("/signUp/answer")
    @Operation(description = "회원가입 시 작성한 질문을 저장한다.")
    public ResponseEntity<?> saveAnswers(
            @AuthenticatedAuthUser AuthUser signUpUser, @Valid @RequestBody List<AnswerDto> answers) {
        answerService.saveAnswers(answers, signUpUser.getProfile().getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
