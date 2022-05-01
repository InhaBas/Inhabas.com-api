package com.inhabas.api.controller;

import com.inhabas.api.auth.utils.argumentResolver.Authenticated;
import com.inhabas.api.domain.member.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    /* token authentication */

    @GetMapping("${authenticate.oauth2-success-handle-url}")
    @Operation(description = "로그인 성공하여 최종적으로 accessToken, refreshToken 을 발행한다.", hidden = true)
    public ResponseEntity<?> successLogin(
            HttpServletRequest request, @Authenticated LoginMember authUserDetail) throws URISyntaxException {

        request.getSession().invalidate();
        throw new NotImplementedException("소셜로그인 다시 구현해야함");

        //return new ResponseEntity<>(HttpStatus.SEE_OTHER);
    }



    @GetMapping("${authenticate.oauth2-failure-handle-url}")
    @Operation(hidden = true)
    public ResponseEntity<?> failToLogin() {
        Map<String, String> message = new HashMap<>() {{
            put("message","fail_to_social_login");
        }};
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}
