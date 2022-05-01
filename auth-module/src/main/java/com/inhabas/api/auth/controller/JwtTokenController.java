package com.inhabas.api.auth.controller;

import com.inhabas.api.auth.domain.token.TokenDto;
import com.inhabas.api.auth.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class JwtTokenController {

    private final TokenService tokenService;

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
