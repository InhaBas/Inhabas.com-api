package com.inhabas.api.controller;

import com.inhabas.api.dto.signUp.StudentSignUpForm;
import com.inhabas.api.security.argumentResolver.AuthenticatedAuthUser;
import com.inhabas.api.security.domain.AuthUser;
import com.inhabas.api.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final MemberService memberService;

    /* sign up new member */

    @PostMapping("/signUp/student")
    @Operation(description = "회원가입 시 프로필을 저장한다.")
    public ResponseEntity<?> saveProfile(@AuthenticatedAuthUser AuthUser authUser, @Valid @RequestBody StudentSignUpForm form) {
        memberService.signUp(authUser, form);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/signUp/student")
    @Operation(description = "임시저장한 개인정보를 불러온다.")
    public ResponseEntity<StudentSignUpForm> loadProfile(@AuthenticatedAuthUser AuthUser signUpUser) {
        StudentSignUpForm form = memberService.loadSignUpForm(signUpUser.getProfile().getId(), signUpUser.getEmail());

        return ResponseEntity.ok(form);
    }

    @PostMapping("/signUp/questionnaire")
    @Operation(description = "회원가입 시 작성한 질문을 저장한다.")
    public ResponseEntity<?> saveQuestionnaire() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
