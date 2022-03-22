package com.inhabas.api.controller;

import com.inhabas.api.security.utils.argumentResolver.Authenticated;
import com.inhabas.api.security.domain.authUser.AuthUserDetail;
import com.inhabas.api.service.login.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;

    /* token authentication */

    @GetMapping("${authenticate.oauth2-success-handle-url}")
    @Operation(description = "로그인 성공하여 최종적으로 accessToken, refreshToken 을 발행한다.", hidden = true)
    public ResponseEntity<?> successLogin(
            HttpServletRequest request, @Authenticated AuthUserDetail authUserDetail) throws URISyntaxException {

        request.getSession().invalidate();
        HttpHeaders httpHeaders = loginService.prepareRedirectHeader(request, authUserDetail);

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
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
