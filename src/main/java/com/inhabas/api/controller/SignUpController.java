package com.inhabas.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    /* sign up new member */

    @PostMapping("/signUp/profile")
    @Operation(description = "회원가입 시 프로필을 저장한다.")
    public ResponseEntity<?> saveProfile() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/signUp/questionnaire")
    @Operation(description = "회원가입 시 작성한 질문을 저장한다.")
    public ResponseEntity<?> saveQuestionnaire() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
