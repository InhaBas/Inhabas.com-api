package com.inhabas.api.controller;

import com.inhabas.api.security.domain.TokenService;
import com.inhabas.api.security.jwtUtils.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class JwtTokenController {

    private final TokenService tokenService;

    @GetMapping("${authenticate.invalid-jwt-token-handle-url}")
    @Operation(summary = "토큰 유효성 검사에 실패했다.", hidden = true)
    public ResponseEntity<String> invalidToken() {

        return new ResponseEntity<>("invalid_token", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/jwt/reissue-token")
    @Operation(summary = "access token 재발급을 요청한다.", description = "request 헤더 Authenticate 에 refreshToken 넣어서 보내줘야함.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 refreshToken")
    })
    public ResponseEntity<TokenDto> reissueAccessToken(HttpServletRequest request) {

        TokenDto newAccessToken = tokenService.reissueAccessToken(request);

        return ResponseEntity.ok(newAccessToken);
    }
}
